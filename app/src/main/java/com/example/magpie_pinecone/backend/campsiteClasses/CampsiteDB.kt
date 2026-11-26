package com.example.magpie_pinecone.backend.campsiteClasses
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.example.magpie_pinecone.backend.sharedDataClasses.ImageData
import com.example.magpie_pinecone.backend.uploadImageToFirebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
/**
 * Provides actions for interfacing with the campsite database
 *
 */
// TODO: Add support for images
class CampsiteDB {
    private val db = FirebaseFirestore.getInstance()
    private val campsitesCollection = db.collection("campsites")
    private val campsiteDbScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun uploadCampsiteImage(imageUri: Uri, onResult: (String?) -> Unit) {
        campsiteDbScope.launch {
            onResult(uploadImageToFirebase(imageUri).getOrNull())
        }
    }

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
        try {
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
                }
        } catch (e: Exception) {
            Log.e("CampsiteDB", "Invalid campsite ID format: $campsiteID", e)
            onResult(null)
        }
    }
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
        searchCampsitesInRange(
            name,
            location,
            date,
            date,
            guests,
            onResult
        )
    }

    /**
     * Fetches campsites that are available throughout the selected date range
     * and match the other search filters.
     */
    fun searchCampsitesInRange(
        name: String,
        location: String,
        startDate: Date?,
        endDate: Date?,
        guests: Int?,
        onResult: (List<Campsite>) -> Unit
    ) {
        val startMillis = startDate?.let { localDateToUtcMillis(it) }
        val endMillis = endDate?.let { localDateToUtcMillis(it) }

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

                val matchesDate = when {
                    startMillis == null && endMillis == null -> true

                    startMillis != null && endMillis != null -> {
                        val availableDays = campsite.availableDates
                            .map { localDateToUtcMillis(it) }
                            .toSet()

                        hasAvailableDateRange(
                            availableDays,
                            startMillis,
                            endMillis
                        )
                    }

                    else -> false
                }

                matchesName &&
                        matchesLocation &&
                        matchesGuests &&
                        matchesDate
            }

            onResult(filteredCampsites)
        }
    }
}

/**
 * Converts a local date to midnight UTC so calendar days can be compared.
 */
internal fun localDateToUtcMillis(date: Date): Long {
    val local = Calendar.getInstance().apply {
        time = date
    }

    return Calendar.getInstance(
        TimeZone.getTimeZone("UTC")
    ).apply {
        clear()
        set(
            local.get(Calendar.YEAR),
            local.get(Calendar.MONTH),
            local.get(Calendar.DAY_OF_MONTH)
        )
    }.timeInMillis
}

/**
 * Checks whether every day in the selected date range is available.
 * Both the start and end dates are included.
 */
internal fun hasAvailableDateRange(
    availableDays: Set<Long>,
    startMillis: Long,
    endMillis: Long
): Boolean {
    if (startMillis > endMillis) {
        return false
    }

    var day = startMillis

    while (day <= endMillis) {
        if (day !in availableDays) {
            return false
        }

        // Move to the next UTC calendar day
        day += 24 * 60 * 60 * 1000L
    }

    return true
}