package com.example.magpie_pinecone.backend.entrantClasses

import com.example.magpie_pinecone.backend.UserClasses.AccountType
import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.backend.reservationClasses.Reservation

/**
 * Stores the data associated with an entrant user of the application.
 *
 * @property activeReservations This entrant’s active reservations.
 * @property pastReservations This entrant’s past reservations.
 * @property notifOptOut Whether this entrant has opted out of notifications.
 * @constructor Creates an Entrant class.
 */
class Entrant (
    id: String = "",
    name: String = "",
    email: String = "",
    phoneNumber: String = "",
    var activeReservations: MutableList<Reservation> = mutableListOf<Reservation>(),
    var pastReservations: MutableList<Reservation> = mutableListOf<Reservation>(),
    var notifOptOut: Boolean,
): User(id, name, email, phoneNumber, accountType = AccountType.Entrant) {
}