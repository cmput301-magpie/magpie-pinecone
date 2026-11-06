package com.example.magpie_pinecone.backend.ownerClasses

import android.app.Notification
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.campsiteClasses.CampsiteDB
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.backend.lotteryClasses.LotteryDB
import com.example.magpie_pinecone.backend.reservationClasses.Reservation

/**
 * Provides actions for owners when interacting with the app.
 * @constructor Creates an OwnerHandler.
 */
class OwnerHandler() {
    /**
     * Adds a campsite with properties defined by the Owner into the provided campsite database.
     */
    fun registerCampsite(
        campsites: CampsiteDB,
        campsite: Campsite
    ) {
        campsites.addCampsite(campsite)
    }

    /**
     * Updates the details of a Campsite registered by the Owner.
     */
    fun updateCampsiteDetails(
        campsites: CampsiteDB,
        campsite: Campsite
    ) {
        campsites.updateCampsite(campsite)
    }

    /**
     * Creates a lottery.
     */
    fun createLottery(
        lotteries: LotteryDB,
        lottery: Lottery
    ) {
        lotteries.addLottery(lottery)
    }

    /**
     * Updates a lottery created by the Owner.
     */
    fun updateLottery(
        lotteries: LotteryDB,
        lottery: Lottery,
    ) {
        lotteries.updateLottery(lottery)
    }

    /**
     * Rejects a Reservation made by an Entrant.
     */
    fun rejectReservation(reservation: Reservation): Unit { TODO() }

//    /**
//     * Sends a notification to an Entrant.
//     */
//    fun sendNotif(handler: NotificationHandler): Unit { TODO() }

//    /**
//     * Sends a message to the Entrant.
//     */
//    fun sendMsg(message: Message, database: MessageDB, notification: Notification): Unit { TODO() }
}