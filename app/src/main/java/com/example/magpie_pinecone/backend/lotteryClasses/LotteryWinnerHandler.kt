package com.example.magpie_pinecone.backend.lotteryClasses

import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.reservationClasses.Reservation
import java.util.Date


/**
 * Actions for LotteryWinners to either book the campsite or to decline.
 */
class LotteryWinnerHandler {
    /**
     * Gives the option for [LotteryWinner] to accept or reject the lottery (true for accept, false for reject).
     * @return The boolean value of reserve or decline
     */
    fun lotteryDecision(lotteryWinner: LotteryWinner, decision: Boolean): Unit{
        if (decision){
            lotteryWinner.winnerStatus = LotteryWinnerStatus.Accepted

        } else {
            lotteryWinner.winnerStatus = LotteryWinnerStatus.Rejected

        }
    }

    /**
     * Allows LotteryWinner to select one of the [campsite] in the lottery to create a reserveration.
     * @return The selected campsite.
     */
    fun selectCampsite(lotteryWinner: LotteryWinner, campsite: Campsite){
        lotteryWinner.selectedCampsite = campsite
    }


    /**
     * Allows LotteryWinner to choose which dates to reserve a [campsite].
     * @return Reservation
     */
    fun chooseReserveDate(startDate: Date, endDate: Date): Unit {
        // Use LotteryWinner.selectedCampsite for this
        return TODO("Provide the return value")
    }

}