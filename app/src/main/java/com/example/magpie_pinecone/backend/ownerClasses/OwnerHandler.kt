package com.example.magpie_pinecone.backend.ownerClasses

import android.app.Notification
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.campsiteClasses.CampsiteDB
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.backend.lotteryClasses.LotteryDB
import com.example.magpie_pinecone.backend.reservationClasses.Reservation

/**
 * Provides actions for owners when interacting with the app
 */

class OwnerHandler() {
    /**
     * Creates a campsite with properties defined by the Owner
     */
    fun registerCampsite(campsites: CampsiteDB): Campsite {
        return TODO("Provide the return value")
    }

    /**
     * Updates the details of a Campsite set by Owner
     */
    fun updateCampsiteDetails(campsite: Campsite): Unit {}

    /**
     * Creates a lottery
     */
    fun createLottery(campsites: List<Campsite>,lotteries: LotteryDB): Lottery {
        return TODO("Provide the return value")
    }

    /**
     * Updates a lottery created by Owner
     */
    fun updateLottery(lottery: Lottery): Unit {}

    /**
     * Rejects a Reservation made by an Entrant
     */
    fun rejectReservation(reservation: Reservation): Unit {}

    /**
     * Sends a notification to an Entrant
     */
    fun sendNotif(handler: NotificationHandler): Unit {}

    /**
     * Sends a message to the Entrant
     */
    fun sendMsg(message: Message, database: MessageDB, notification: Notification): Unit {}
}