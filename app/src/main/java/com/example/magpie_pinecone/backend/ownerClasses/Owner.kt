package com.example.magpie_pinecone.backend.ownerClasses

import android.app.Notification
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.backend.reservationClasses.Reservation

/**
 * Stores the data associated with an owner user of the application.
 *
 * @property campsites The campsites listed by this owner
 * @property lotteries The lotteries associated with this owner’s campsites
 * @property activeReservations The active reservations associated with this owner’s campsites
 * @property cancelledReservations The cancelled reservations associated with this owner’s campsites
 * @property pastReservations The past reservations of this owner’s campsites
 * @property sentNotifications The notifications sent by this owner
 * @constructor Creates an Owner class
 */


class Owner(
    var campsites: List<Campsite>,
    var lotteries: List<Lottery>,
    var activeReservations: List<Reservation>,
    var cancelledReservations: List<Reservation>,
    var pastReservations: List<Reservation>,
    var sentNotifications: List<Notification>
    ) {
}