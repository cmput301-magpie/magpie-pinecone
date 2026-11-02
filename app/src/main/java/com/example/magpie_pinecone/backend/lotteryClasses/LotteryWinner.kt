package com.example.magpie_pinecone.backend.lotteryClasses

import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.entrantClasses.Entrant

/**
 * Stores data associated with the winner of a lottery.
 *
 * @property entrant The entrant that won the lottery.
 * @property lottery The lottery that the entrant won in.
 * @property winnerStatus Whether the winner has accepted the lottery reservation.
 * @property campsites The list of campsite that the winner won a reservation for.
 */

class LotteryWinner (
    val entrant: Entrant,
    val lottery: Lottery,
    val campsites: List<Campsite>
) {
    val winnerStatus = Boolean

}