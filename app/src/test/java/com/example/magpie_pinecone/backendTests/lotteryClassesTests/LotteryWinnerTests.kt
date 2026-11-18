package com.example.magpie_pinecone.backendTests.lotteryClassesTests

import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.entrantClasses.Entrant
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.backend.lotteryClasses.LotteryWinner
import com.example.magpie_pinecone.backend.lotteryClasses.LotteryWinnerStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LotteryWinnerTests {

    @Test
    fun baseConstructors() {
        val winner = LotteryWinner()   // Firestore needs every class object to have base constructors

        assertEquals("", winner.id)
        assertNull(winner.entrant)
        assertNull(winner.lottery)
        assertTrue(winner.campsites.isEmpty())
        assertNull(winner.selectedCampsite)
        assertEquals(LotteryWinnerStatus.Pending, winner.winnerStatus)   // starts as Pending
    }

    @Test
    fun valuedConstructors() {
        val entrant = Entrant(id = "entrant123")
        val lottery = Lottery(id = "lottery123")
        val campsite1 = Campsite()
        val campsite2 = Campsite()

        val winner = LotteryWinner(
            id = "winner123",
            entrant = entrant,
            lottery = lottery,
            campsites = listOf(campsite1, campsite2),
            selectedCampsite = campsite1,
            winnerStatus = LotteryWinnerStatus.Accepted
        )

        assertEquals("winner123", winner.id)
        assertEquals(entrant, winner.entrant)
        assertEquals(lottery, winner.lottery)
        assertEquals(listOf(campsite1, campsite2), winner.campsites)
        assertEquals(campsite1, winner.selectedCampsite)
        assertEquals(LotteryWinnerStatus.Accepted, winner.winnerStatus)
    }

    @Test
    fun winner_fieldsCanBeUpdated() {
        val winner = LotteryWinner()
        val campsite = Campsite()

        // id, selectedCampsite and winnerStatus are "var", so they can be changed later
        winner.id = "newId"
        winner.selectedCampsite = campsite
        winner.winnerStatus = LotteryWinnerStatus.Rejected

        assertEquals("newId", winner.id)
        assertEquals(campsite, winner.selectedCampsite)
        assertEquals(LotteryWinnerStatus.Rejected, winner.winnerStatus)
    }

    @Test
    fun winnerStatus_hasThreeTypes() {
        val statuses = LotteryWinnerStatus.entries

        // There should be exactly Accepted, Rejected and Pending, in that order
        assertEquals(3, statuses.size)
        assertEquals(LotteryWinnerStatus.Accepted, statuses[0])
        assertEquals(LotteryWinnerStatus.Rejected, statuses[1])
        assertEquals(LotteryWinnerStatus.Pending, statuses[2])
    }
}