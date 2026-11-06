package com.example.magpie_pinecone.backend.lotteryClasses

import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.entrantClasses.Entrant

/**
 * Stores whether the LotteryWinner has accepted the reservation, rejected it, or has not decided yet
 */
enum class LotteryWinnerStatus{
    Accepted,
    Rejected,
    Pending,
}


/**
 * Stores data associated with the winner of a lottery.
 *
 * @property entrant The entrant that won the lottery.
 * @property lottery The lottery that the entrant won in.
 * @property winnerStatus Whether the winner has accepted (or rejected) the lottery reservation.
 * @property campsites The list of campsite that the winner won a reservation for.
 * @property selectedCampsite The Campsite that the winner has selected to reserve.
 */

data class LotteryWinner (
    var id: String = "",
    val entrant: Entrant? = null,
    val lottery: Lottery? = null,
    val campsites: List<Campsite> = listOf<Campsite>(),
    var selectedCampsite: Campsite? = null,
    var winnerStatus: LotteryWinnerStatus = LotteryWinnerStatus.Pending,
) {


}