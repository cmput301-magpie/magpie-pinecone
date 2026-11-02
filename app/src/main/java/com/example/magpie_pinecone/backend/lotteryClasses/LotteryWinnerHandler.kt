package com.example.magpie_pinecone.backend.lotteryClasses

import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.reservationClasses.Reservation


/**
 * Actions for LotteryWinners to either book the campsite or decline
 */
class LotteryWinnerHandler {
    /**
     * Gives the option for LotteryWinner to reserve or to decline the [lottery].
     * @return the boolean value of reserve or decline
     */
    fun lotteryDecision(lottery: Lottery): Boolean{
        return TODO("Provide the return value")
    }


    /**
     * Allows LotteryWinner to select one of the campsites in the [lottery] to reserve.
     * @return campsite selected
     */
    fun selectCampsite(lottery: Lottery): Campsite{
        return TODO("Provide the return value")
    }


    /**
     * Allows LotteryWinner to choose which dates to reserve a [campsite].
     * @return Reservation
     */
    fun chooseReserveDate(campsite: Campsite): Reservation {
        return TODO("Provide the return value")
    }

}