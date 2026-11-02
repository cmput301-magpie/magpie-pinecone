package com.example.magpie_pinecone.backend.entrantClasses

import com.example.magpie_pinecone.backend.reservationClasses.Reservation

/**
 * Stores the data associated with an entrant user of the application.
 *
 * @property activeReservations This entrant’s active reservations.
 * @property pastReservations This entrant’s past reservations.
 * @property notifOptOut Whether this entrant has opted out of notifications.
 * @property profile The profile of this entrant.
 * @constructor Creates an Entrant class.
 */
class Entrant (var activeReservations: List<Reservation>, var pastReservations: List<Reservation>,
    var notifOptOut: Boolean, var profile: TBD) {
}