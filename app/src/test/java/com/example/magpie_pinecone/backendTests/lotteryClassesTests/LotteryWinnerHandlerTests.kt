package com.example.magpie_pinecone.backendTests.lotteryClassesTests

import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.lotteryClasses.LotteryWinner
import com.example.magpie_pinecone.backend.lotteryClasses.LotteryWinnerHandler
import com.example.magpie_pinecone.backend.lotteryClasses.LotteryWinnerStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class LotteryWinnerHandlerTests {

    @Test
    fun lotteryDecision_acceptSetsAccepted() {
        val handler = LotteryWinnerHandler()
        val winner = LotteryWinner()

        // true means the winner accepts the reservation
        handler.lotteryDecision(winner, true)

        assertEquals(LotteryWinnerStatus.Accepted, winner.winnerStatus)
    }

    @Test
    fun lotteryDecision_rejectSetsRejected() {
        val handler = LotteryWinnerHandler()
        val winner = LotteryWinner()

        // false means the winner rejects the reservation
        handler.lotteryDecision(winner, false)

        assertEquals(LotteryWinnerStatus.Rejected, winner.winnerStatus)
    }

    @Test
    fun lotteryDecision_canChangeMind() {
        val handler = LotteryWinnerHandler()
        val winner = LotteryWinner()

        // Accept first, then reject: the latest decision should win
        handler.lotteryDecision(winner, true)
        handler.lotteryDecision(winner, false)

        assertEquals(LotteryWinnerStatus.Rejected, winner.winnerStatus)
    }

    @Test
    fun selectCampsite_setsSelectedCampsite() {
        val handler = LotteryWinnerHandler()
        val campsite = Campsite(name = "Lakeview")
        val winner = LotteryWinner(campsites = listOf(campsite))

        handler.selectCampsite(winner, campsite)

        assertEquals(campsite, winner.selectedCampsite)
    }

    @Test
    fun selectCampsite_replacesOldChoice() {
        val handler = LotteryWinnerHandler()
        val first = Campsite(name = "Lakeview")
        val second = Campsite(name = "Hilltop")
        val winner = LotteryWinner(campsites = listOf(first, second))

        // Pick one, then change to the other
        handler.selectCampsite(winner, first)
        handler.selectCampsite(winner, second)

        assertEquals(second, winner.selectedCampsite)
    }
}