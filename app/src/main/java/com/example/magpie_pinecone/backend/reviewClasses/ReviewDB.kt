package com.example.magpie_pinecone.backend.reviewClasses

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class ReviewDB {
    private val db = FirebaseFirestore.getInstance()
    private val reviewCollection = db.collection("reviews")
    private val campsiteCollection = db.collection("campsites")

    /**
     * Saves [review] to the database and updates the campsite's average rating in one transaction.
     * If the entrant already reviewed this campsite, the old review is replaced and only the
     * difference in rating is added to the total. [onResult] receives true on success, false on failure.
     */
    fun submitReview(review: Review, onResult: (Boolean) -> Unit) {
        // Review must have a campsiteID and entrantID associated with it. Prevent submitting if either is missing!
        if (review.campsiteID.isEmpty() || review.entrantID.isEmpty()) {
            Log.e("ReviewDB", "Cannot submit review without campsite and entrant IDs")
            onResult(false)
        }

        // Review IDs are in the format "[campsiteID]_[entrantID]" for easy reference
        // Each entrant can only have one review per campsite
        review.ID = "${review.campsiteID}_${review.entrantID}"
        review.createdAt = Date()  // Review created at current date

        val reviewDoc = reviewCollection.document(review.ID)
        val campsiteDoc = campsiteCollection.document(review.campsiteID)

        db.runTransaction { transaction ->
            val old = transaction.get(reviewDoc).toObject(Review::class.java)
            val campsite = transaction.get(campsiteDoc)

            var total = (campsite.getLong("ratingTotal") ?: 0L).toInt()
            var count = (campsite.getLong("ratingCount") ?: 0L).toInt()

            if (old == null) {
                total += review.rating
                count += 1
            } else {
                total += review.rating - old.rating  // updating reviewTotal with new rating, removing old rating from total.
            }

            transaction.set(reviewDoc, review)
            transaction.update(
                campsiteDoc,
                mapOf(
                    "ratingTotal" to total,
                    "ratingCount" to count,
                    "rating" to total.toDouble() / count
                )
            )
            null
        }
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { e ->
                Log.e("ReviewDB", "Failed to submit review", e)
                onResult(false)
            }
    }

    /**
     * Fetches the reviews of a [campsiteID], newest first.
     */
    fun getReviewsByCampsite(campsiteID: String, onResult: (List<Review>) -> Unit) {
        reviewCollection
            .whereEqualTo("campsiteID", campsiteID)
            .get()
            .addOnSuccessListener { snapshot ->

                val reviews = snapshot.documents
                    .mapNotNull { it.toObject(Review::class.java) }
                    .sortedByDescending { it.createdAt }

                onResult(reviews)
            }
            .addOnFailureListener { e ->
                Log.e("ReviewDB", "Failed to read reviews", e)
                onResult(emptyList())
            }
    }

    /**
     * Fetches the review [entrantID] wrote for [campsiteID], or null if there isn't one.
     * Used to fill the form when the entrant edits their review.
     */
    fun getReview(campsiteID: String, entrantID: String, onResult: (Review?) -> Unit) {
        reviewCollection.document("${campsiteID}_$entrantID").get()
            .addOnSuccessListener { snapshot ->
                onResult(if (snapshot.exists()) snapshot.toObject(Review::class.java) else null)
            }
            .addOnFailureListener { e ->
                Log.e("ReviewDB", "Failed to read review", e)
                onResult(null)
            }
    }
}