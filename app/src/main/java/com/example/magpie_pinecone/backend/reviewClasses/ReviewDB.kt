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

            var total = updatedRatingTotal(
                total = (campsite.getLong("ratingTotal") ?: 0L).toInt(),
                newRating = review.rating,
                oldRating = old?.rating
            )
            var count = updatedRatingCount(
                count = (campsite.getLong("ratingCount") ?: 0L).toInt(),
                isNewReview = old == null
            )

            transaction.set(reviewDoc, review)
            transaction.update(
                campsiteDoc,
                mapOf(
                    "ratingTotal" to total,
                    "ratingCount" to count,
                    "rating" to averageRating(total, count)
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

/**
 * Creates the document ID of a review for easy readability. There is one review per entrant
 * per campsite, so posting again edits the existing review instead of adding a second one.
 */
internal fun reviewDocumentId(campsiteID: String, entrantID: String): String {
    return "${campsiteID}_$entrantID"
}

/**
 * The new sum of all review ratings after a review is posted.
 * A new review ([oldRating] is null) adds its rating. An edited review swaps its old rating
 * for the new one.
 */
internal fun updatedRatingTotal(total: Int, newRating: Int, oldRating: Int?): Int {
    return total + newRating - (oldRating ?: 0)
}

/**
 * The new number of reviews after a review is posted.
 * Only a brand new review raises the count; editing a review leaves it the same.
 */
internal fun updatedRatingCount(count: Int, isNewReview: Boolean): Int {
    return if (isNewReview) count + 1 else count
}

/**
 * The average rating, or 0.0 when there are no reviews (avoids dividing by zero).
 */
internal fun averageRating(total: Int, count: Int): Double {
    return if (count == 0) 0.0 else total.toDouble() / count
}
