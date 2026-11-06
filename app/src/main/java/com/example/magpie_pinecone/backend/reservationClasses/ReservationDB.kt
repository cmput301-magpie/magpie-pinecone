package com.example.magpie_pinecone.backend.reservationClasses

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore


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
                Log.e("ReservationDB", "Failed to add reservation")
            }
    }

    /**
     * Deletes the specified [reservation] from this reservation database. Only attempts deletion if the specified reservation currently exists in the lottery database.
     */
    fun deleteReservation(reservation: Reservation) {

    }

    /**
     * Updates the specified [reservation]'s details in this reservation database. Only attempts replacement if the specified reservation currently exists in the reservation database
     */
    fun updateReservation(reservation: Reservation) { TODO() }

    /**
     * Gets the reservation with the matching [reservationID] from the database.
     * @return The specified reservation instance.
     */
    fun readReservation(reservationID: String): Reservation { TODO() }
}