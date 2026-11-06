package com.example.magpie_pinecone.backend.reservationClasses

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

    }

    /**
     * Deletes the specified [reservation] from this reservation database. Only attempts deletion if the specified reservation currently exists in the lottery database.
     */
    fun deleteReservation(reservation: Reservation) { TODO() }

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