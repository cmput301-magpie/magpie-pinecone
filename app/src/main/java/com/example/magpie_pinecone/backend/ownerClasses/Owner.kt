package com.example.magpie_pinecone.backend.ownerClasses

import android.app.Notification
import com.example.magpie_pinecone.backend.UserClasses.AccountType
import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.backend.reservationClasses.Reservation
import kotlin.collections.listOf

/**
 * Stores the data associated with an owner user of the application.
 *
 * @property campsites The campsites listed by this owner
 * @property lotteries The lotteries associated with this owner’s campsites
 * @property activeReservations The active reservations associated with this owner’s campsites
 * @property cancelledReservations The canceled reservations associated with this owner’s campsites
 * @property pastReservations The past reservations of this owner’s campsites
 * @property sentNotifications The notifications sent by this owner
 * @constructor Creates an Owner class
 */
class Owner(
    id: String = "",
    name: String = "",
    email: String = "",
    phoneNumber: String = "",
    var campsites: MutableList<Campsite> = mutableListOf<Campsite>(),
    var lotteries: MutableList<Lottery> = mutableListOf<Lottery>(),
    var activeReservations: MutableList<Reservation> = mutableListOf<Reservation>(),
    var cancelledReservations: MutableList<Reservation> = mutableListOf<Reservation>(),
    var pastReservations: MutableList<Reservation> = mutableListOf<Reservation>(),
    var sentNotifications: MutableList<Notification> = mutableListOf<Notification>()
): User(id, name, email, phoneNumber, accountType = AccountType.Owner)