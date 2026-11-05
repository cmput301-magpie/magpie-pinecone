package com.example.magpie_pinecone.backend.campsiteClasses
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
/**
 * Provides actions for interfacing with the campsite database
 *
 */

class CampsiteDB {
    private val db = FirebaseFirestore.getInstance()
    private val campsitesCollection = db.collection("campsites")

    /**
     * Adds a new [campsite] to the database
     */
    fun addCampsite(campsite: Campsite) {
        val document = campsitesCollection.document()

        campsite.ID = document.id

        document.set(campsite)
            .addOnSuccessListener {
                Log.d("CampsiteDB", "Campsite added with ID: ${campsite.ID}")
            }
            .addOnFailureListener { exception ->
                Log.e("CampsiteDB", "Failed to add campsite", exception)
            }


    }

    /**
     * Deletes a [campsite] from the database
     */
    fun deleteCampsite(campsite: Campsite) {

        if (campsite.ID.isEmpty()) {
            Log.e("CampsiteDB", "Cannot delete campsite with empty ID")
            return
        }

        campsitesCollection.document(campsite.ID)
            .delete()
            .addOnSuccessListener {
                Log.d("CampsiteDB", "Campsite deleted: ${campsite.ID}")
            }
    }

    /**
     * Updates a [campsite] in the database with the details in the given [Campsite]
     */
    fun updateCampsite(campsite: Campsite) {

        if (campsite.ID.isEmpty()) {
            Log.e("CampsiteDB", "Cannot update campsite with empty ID")
            return
        }

        campsitesCollection.document(campsite.ID)
            .set(campsite)
            .addOnSuccessListener {
                Log.d("CampsiteDB", "Campsite updated: ${campsite.ID}")
            }
    }

    /**
     * Fetch a [campsite] from the database
     */
    fun readCampsite(
        campsiteID: String,
        onResult: (Campsite?) -> Unit
    ) {
        val document = campsitesCollection.document(campsiteID)

        document.get()
            .addOnSuccessListener { snapshot ->

                if (snapshot.exists()) {
                    val campsite = snapshot.toObject(Campsite::class.java)
                    onResult(campsite)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("CampsiteDB", "Failed to read campsite", exception)
                onResult(null)
            }}
/**
 * Fetches all campsites from the database
 */
fun getAllCampsites(
    onResult: (List<Campsite>) -> Unit
) {
    campsitesCollection.get()
        .addOnSuccessListener { snapshot ->

            val campsites = snapshot.documents.mapNotNull { document ->
                document.toObject(Campsite::class.java)
            }

            onResult(campsites)
        }


}}

