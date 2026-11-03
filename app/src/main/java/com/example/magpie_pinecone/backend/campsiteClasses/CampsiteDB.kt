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
    fun deleteCampsite(campsite: Campsite): Unit {}

    /**
     * Updates a [campsite] in the database with the details in the given [Campsite]
     */
    fun updateCampsite(campsite: Campsite): Unit {}

    /**
     * Fetch a [campsite] from the database
     */
    fun readCampsite(campsite: Campsite): Unit {}
}