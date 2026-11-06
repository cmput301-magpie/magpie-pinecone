package com.example.magpie_pinecone.backend.campsiteClasses
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import java.util.Calendar
import java.util.Date
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
    }

    /**
     * Fetches campsites by a specified [ownerId] from the database
     */
    fun getCampsitesByOwner(
        ownerId: String,
        onResult: (List<Campsite>) -> Unit
    ) {
        campsitesCollection
            .whereEqualTo("ownerId", ownerId)
            .get()
            .addOnSuccessListener { snapshot ->

                val campsites = snapshot.documents.mapNotNull { document ->
                    document.toObject(Campsite::class.java)
                }

                onResult(campsites)
            }
    }
    /**
     * Fetches campsites that match the given search filters.
     * Blank or null filters are ignored.
     */
    fun searchCampsites(
        name: String,
        location: String,
        date: Date?,
        guests: Int?,
        onResult: (List<Campsite>) -> Unit
    ) {
        getAllCampsites { campsites ->

            val filteredCampsites = campsites.filter { campsite ->

                val matchesName =
                    name.isBlank() ||
                            campsite.name.contains(name, ignoreCase = true)

                val matchesLocation =
                    location.isBlank() ||
                            campsite.location.contains(location, ignoreCase = true)

                val matchesGuests =
                    guests == null ||
                            guests <= campsite.maxOccupancy

                val matchesDate =
                    date == null ||
                            campsite.availableDates.any { availableDate ->
                                isSameDay(availableDate, date)
                            }

                matchesName &&
                        matchesLocation &&
                        matchesGuests &&
                        matchesDate
            }

            onResult(filteredCampsites)
        }
    }
    /**
     * Checks whether two dates represent the same calendar day.
     */
    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val calendar1 = Calendar.getInstance().apply {
            time = date1
        }

        val calendar2 = Calendar.getInstance().apply {
            time = date2
        }

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }
}

