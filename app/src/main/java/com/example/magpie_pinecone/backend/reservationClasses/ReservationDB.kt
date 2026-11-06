package com.example.magpie_pinecone.backend.reservationClasses

import android.util.Log
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
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
     */
    fun addReservation(reservation: Reservation) {
        val document = reservationCollection.document()

        reservation.ID = document.id

        document.set(reservation)
            .addOnSuccessListener {
                Log.d("ReservationDB", "Successfully added reservation")
            }
            .addOnFailureListener { e ->
                Log.e("ReservationDB", "Failed to add reservation", e)
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

        reservationCollection
            .whereEqualTo("ownerID", ownerID)
            .whereLessThan("endDate", currentDate)
            .get()
            .addOnSuccessListener { snapshot ->

                val reservations = snapshot.documents.mapNotNull { document ->
                    document.toObject(Reservation::class.java)
                }

                onResult(reservations)
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

        reservationCollection
            .whereEqualTo("ownerID", ownerID)
            .whereGreaterThanOrEqualTo("endDate", currentDate)
            .get()
            .addOnSuccessListener { snapshot ->

                val reservations = snapshot.documents.mapNotNull { document ->
                    document.toObject(Reservation::class.java)
                }

                onResult(reservations)
            }
    }

    /**
     * Fetches cancelled reservations by a specified [ownerID] from the database
     */
    fun getCancelledReservationsByOwner(
        ownerID: String,
        onResult: (List<Reservation>) -> Unit
    ) {
        reservationCollection
            .whereEqualTo("ownerID", ownerID)
            .whereEqualTo("isCancelled", true)
            .get()
            .addOnSuccessListener { snapshot ->

                val reservations = snapshot.documents.mapNotNull { document ->
                    document.toObject(Reservation::class.java)
                }

                onResult(reservations)
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

        reservationCollection
            .whereEqualTo("entrantID", entrantID)
            .whereLessThan("endDate", currentDate)
            .get()
            .addOnSuccessListener { snapshot ->

                val reservations = snapshot.documents.mapNotNull { document ->
                    document.toObject(Reservation::class.java)
                }

                onResult(reservations)
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

        reservationCollection
            .whereEqualTo("entrantID", entrantID)
            .whereGreaterThanOrEqualTo("endDate", currentDate)
            .get()
            .addOnSuccessListener { snapshot ->

                val reservations = snapshot.documents.mapNotNull { document ->
                    document.toObject(Reservation::class.java)
                }

                onResult(reservations)
            }
    }
}