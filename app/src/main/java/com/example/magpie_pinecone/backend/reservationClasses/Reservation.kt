package com.example.magpie_pinecone.backend.reservationClasses

import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.entrantClasses.Entrant
import com.example.magpie_pinecone.backend.ownerClasses.Owner
import java.sql.Date

/**
 * Stores the data associated with a reservation.
 *
 * @property entrant The entrant who booked this reservation.
 * @property owner The owner of the campsite of this reservation.
 * @property campsite The campsite of this reservation.
 * @property startDate The start date of this reservation.
 * @property endDate The end date of this reservation.
 * @property numGuests The number of guests in this reservation.
 * @property termsAndConditions The terms and conditions of this reservation.
 * @constructor Creates a Reservation.
 */
class Reservation(
    var entrant: Entrant,
    var owner: Owner,
    var campsite: Campsite,
    var startDate: Date,
    var endDate: Date,
    var numGuests: Int,
    var termsAndConditions: Boolean
)