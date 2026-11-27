package com.example.magpie_pinecone.backend.reservationClasses

import android.util.Log
import com.example.magpie_pinecone.backend.entrantClasses.Entrant
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date


/**
 * Provides actions for interfacing with the reservation database.
 *
 * @property reservations The list of reservations.
 * @constructor Creates a local instance of the reservation database.
 */
class ReservationDB {
    private val db = FirebaseFirestore.getInstance()
    private val reservationCollection = db.collection("reservations")

    /**
     * Adds a [reservation] to the reservation database.
     *
     * [onResult] is called with true once Firestore confirms the write, or false if it failed.
     */
    fun addReservation(
        reservation: Reservation,
        onResult: (Boolean) -> Unit = {}
    ) {
        val document = reservationCollection.document()

        reservation.ID = document.id

        document.set(reservation)
            .addOnSuccessListener {
                Log.d("ReservationDB", "Successfully added reservation")
                onResult(true)
            }
            .addOnFailureListener { e ->
                Log.e("ReservationDB", "Failed to add reservation", e)
                onResult(false)
            }
    }

    /**
     * Deletes the specified [reservation] from this reservation database. Only attempts deletion if the specified reservation currently exists in the reservation database.
     */
    fun deleteReservation(reservation: Reservation) {
        if (reservation.ID.isEmpty()) {
            Log.e("ReservationDB", "Cannot delete reservation with empty ID")
            return
        }

        reservationCollection.document(reservation.ID)
            .delete()
            .addOnSuccessListener {
                Log.d("ReservationDB", "Reservation deleted: ${reservation.ID}")
            }
    }

    /**
     * Updates the specified [reservation]'s details in this reservation database. Only attempts replacement if the specified reservation currently exists in the reservation database
     */
    fun updateReservation(
        reservation: Reservation
    ) {
        if (reservation.ID.isEmpty()) {
            Log.e("ReservationDB", "Cannot delete reservation with empty ID")
            return
        }

        reservationCollection.document(reservation.ID)
            .set(reservation)
            .addOnSuccessListener {
                Log.d("ReservationDB", "Reservation updated: ${reservation.ID}")
            }
    }

    /**
     * Gets the reservation with the matching [reservationID] from the database.
     */
    fun readReservation(
        reservationID: String,
        onResult: (Reservation?) -> Unit
    ) {
        reservationCollection.document(reservationID).get()
            .addOnSuccessListener { snapshot ->

                if (snapshot.exists()) {
                    val reservation = snapshot.toObject(Reservation::class.java)
                    onResult(reservation)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("ReservationDB", "Failed to read reservation", e)
                onResult(null)
            }
    }

    /**
     * Fetches past reservations by a specified [ownerID] from the database
     */
    fun getPastReservationsByOwner(
        ownerID: String,
        onResult: (List<Reservation>) -> Unit
    ) {
        val currentDate = Date()

        // Only filter by owner in Firestore. Filtering by owner AND endDate needs a
        // composite index, so we check the date here in Kotlin instead.
        reservationCollection
            .whereEqualTo("ownerID", ownerID)
            .get()
            .addOnSuccessListener { snapshot ->

                val reservations = snapshot.documents
                    .mapNotNull { it.toObject(Reservation::class.java) }
                    .filter { it.endDate.before(currentDate) }   // Keep only reservations that have ended

                onResult(reservations)
            }
            .addOnFailureListener { e ->
                Log.e("ReservationDB", "Failed to read owner's past reservations", e)
                onResult(emptyList())
            }
    }

    /**
     * Fetches active reservations by a specified [ownerID] from the database
     */
    fun getActiveReservationsByOwner(
        ownerID: String,
        onResult: (List<Reservation>) -> Unit
    ) {
        val currentDate = Date()

        // Only filter by owner in Firestore. Filtering by owner AND endDate needs a
        // composite index, so we check the date here in Kotlin instead.
        reservationCollection
            .whereEqualTo("ownerID", ownerID)
            .get()
            .addOnSuccessListener { snapshot ->

                val reservations = snapshot.documents
                    .mapNotNull { it.toObject(Reservation::class.java) }
                    .filter { !it.endDate.before(currentDate) }   // Keep only reservations that haven't ended yet

                onResult(reservations)
            }
            .addOnFailureListener { e ->
                Log.e("ReservationDB", "Failed to read owner's active reservations", e)
                onResult(emptyList())
            }
    }

    /**
     * Fetches cancelled reservations by a specified [ownerID] from the database.
     *
     * Firestore saves the Kotlin property "isCancelled" under the field name "cancelled",
     * so instead of asking Firestore for "isCancelled", we get all of the owner's
     * reservations and keep only the cancelled ones here in Kotlin.
     */
    fun getCancelledReservationsByOwner(
        ownerID: String,
        onResult: (List<Reservation>) -> Unit
    ) {
        reservationCollection
            .whereEqualTo("ownerID", ownerID)
            .get()
            .addOnSuccessListener { snapshot ->

                val reservations = snapshot.documents
                    .mapNotNull { it.toObject(Reservation::class.java) }
                    .filter { it.isCancelled }

                onResult(reservations)
            }
            .addOnFailureListener { e ->
                Log.e("ReservationDB", "Failed to read cancelled reservations", e)
                onResult(emptyList())
            }
    }

    /**
     * Fetches reservations by a specified [entrantID] from the database
     */
    fun getPastReservationsByEntrant(
        entrantID: String,
        onResult: (List<Reservation>) -> Unit
    ) {
        val currentDate = Date()

        // Only filter by entrant in Firestore. Filtering by entrant AND endDate needs a
        // composite index, so we check the date here in Kotlin instead.
        reservationCollection
            .whereEqualTo("entrantID", entrantID)
            .get()
            .addOnSuccessListener { snapshot ->

                val reservations = snapshot.documents
                    .mapNotNull { it.toObject(Reservation::class.java) }
                    .filter { it.endDate.before(currentDate) }   // Keep only reservations that have ended

                onResult(reservations)
            }
            .addOnFailureListener { e ->
                Log.e("ReservationDB", "Failed to read past reservations", e)
                onResult(emptyList())
            }
    }

    /**
     * Fetches active reservations by a specified [entrantID] from the database
     */
    fun getActiveReservationsByEntrant(
        entrantID: String,
        onResult: (List<Reservation>) -> Unit
    ) {
        val currentDate = Date()

        // Only filter by entrant in Firestore. Filtering by entrant AND endDate needs a
        // composite index, so we check the date here in Kotlin instead
        reservationCollection
            .whereEqualTo("entrantID", entrantID)
            .get()
            .addOnSuccessListener { snapshot ->

                val reservations = snapshot.documents
                    .mapNotNull { it.toObject(Reservation::class.java) }
                    .filter { !it.endDate.before(currentDate) }   // Keep only reservations that haven't ended yet

                onResult(reservations)
            }
            .addOnFailureListener { e ->
                Log.e("ReservationDB", "Failed to read active reservations", e)
                onResult(emptyList())
            }
    }

    /**
     * Fetches active (not cancelled, not yet ended) reservations for a specified [campsiteID].
     */
    fun getActiveReservationsByCampsite(
        campsiteID: String,
        onResult: (List<Reservation>) -> Unit
    ) {
        val currentDate = Date()

        // Only filter by campsite in Firestore. Filtering by campsite AND endDate needs a
        // composite index, so we check the date here in Kotlin instead.
        reservationCollection
            .whereEqualTo("campsiteID", campsiteID)
            .get()
            .addOnSuccessListener { snapshot ->

                val reservations = snapshot.documents
                    .mapNotNull { it.toObject(Reservation::class.java) }
                    .filter { !it.isCancelled && !it.endDate.before(currentDate) }   // Not cancelled, not ended yet
                onResult(reservations)
            }
            .addOnFailureListener { e ->
                Log.e("ReservationDB", "Failed to read campsite reservations", e)
                onResult(emptyList())
            }
    }

    /**
     * Checks whether [entrantID] has a non-cancelled reservation for [campsiteID], including past reservations.
     * Used for reviewing eligibility, since only entrants that have reserved the campsite completely should
     * be able to review it.
     */
    fun hasReservedCampsite(
        entrantID: String,
        campsiteID: String,
        onResult: (Boolean) -> Unit
    ) {
        reservationCollection
            .whereEqualTo("entrantID", entrantID)
            .whereEqualTo("campsiteID", campsiteID)
            .get()
            .addOnSuccessListener { snapshot ->
                val reserved = snapshot.documents
                    .mapNotNull { it.toObject(Reservation::class.java) }
                    .any { !it.isCancelled }
                onResult(reserved)
            }
            .addOnFailureListener { e ->
                Log.e("ReservationDB", "Failed to check reservation", e)
                onResult(false)
            }
    }
}