package com.example.magpie_pinecone.backendTests.lotteryClassesTests

import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.entrantClasses.Entrant
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.backend.lotteryClasses.isWaitlistFull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Date

class LotteryTests {

    @Test
    fun baseConstructors() {
        val lottery = Lottery()   // Firestore needs every class object to have base constructors

        assertEquals("", lottery.id)
        assertEquals("", lottery.ownerId)
        assertTrue(lottery.campsites.isEmpty())
        assertEquals(null, lottery.activeStartDate)
        assertEquals(null, lottery.activeEndDate)
        assertEquals(null, lottery.registrationStartDate)
        assertEquals(null, lottery.registrationEndDate)
        assertEquals(null, lottery.bookingStartDate)
        assertEquals(null, lottery.bookingEndDate)
        assertEquals(1, lottery.numberOfWinners)
        assertNull(lottery.waitlistCapacity)
        assertTrue(lottery.waitlist.isEmpty())
        assertTrue(lottery.winners.isEmpty())
    }

    @Test
    fun valuedConstructors() {
        val campsite = Campsite()
        val entrant1 = Entrant()
        val entrant2 = Entrant()

        val activeStart = Date()
        val activeEnd = Date()
        val registrationStart = Date()
        val registrationEnd = Date()
        val bookingStart = Date()
        val bookingEnd = Date()

        val lottery = Lottery(
            id = "lottery123",
            ownerId = "owner123",
            campsites = mutableListOf(campsite),
            activeStartDate = activeStart,
            activeEndDate = activeEnd,
            registrationStartDate = registrationStart,
            registrationEndDate = registrationEnd,
            bookingStartDate = bookingStart,
            bookingEndDate = bookingEnd,
            numberOfWinners = 2,
            waitlistCapacity = 50,
            waitlist = mutableListOf(entrant1, entrant2),
            winners = listOf(entrant1)
        )

        assertEquals("lottery123", lottery.id)
        assertEquals("owner123", lottery.ownerId)

        assertEquals(mutableListOf(campsite), lottery.campsites)

        assertEquals(activeStart, lottery.activeStartDate)
        assertEquals(activeEnd, lottery.activeEndDate)
        assertEquals(registrationStart, lottery.registrationStartDate)
        assertEquals(registrationEnd, lottery.registrationEndDate)
        assertEquals(bookingStart, lottery.bookingStartDate)
        assertEquals(bookingEnd, lottery.bookingEndDate)

        assertEquals(2, lottery.numberOfWinners)
        assertEquals(50, lottery.waitlistCapacity)

        assertEquals(mutableListOf(entrant1, entrant2), lottery.waitlist)
        assertEquals(listOf(entrant1), lottery.winners)
    }

    @Test
    fun twoLotteries_doNotShareLists() {
        val a = Lottery()
        val b = Lottery()

        // Changing lists of a to see if b lists also update
        a.campsites.add(Campsite())
        a.waitlist.add(Entrant())

        assertTrue(b.campsites.isEmpty())
        assertTrue(b.waitlist.isEmpty())
    }

    @Test
    fun generateWinners_picksCorrectNumber() {
        val lottery = Lottery(
            numberOfWinners = 2,
            waitlist = mutableListOf(Entrant(id = "E1"), Entrant(id = "E2"), Entrant(id = "E3"))
        )

        val winners = lottery.generateWinners()

        // Only 2 winners should be picked out of 3 entrants
        assertEquals(2, winners.size)
    }

    @Test
    fun generateWinners_waitlistSmallerThanWinners() {
        val lottery = Lottery(
            numberOfWinners = 5,
            waitlist = mutableListOf(Entrant(id = "E1"), Entrant(id = "E2"))
        )

        val winners = lottery.generateWinners()

        // Only 2 people joined, so everyone wins (no crash, no extra winners)
        assertEquals(2, winners.size)
    }

    @Test
    fun generateWinners_emptyWaitlist() {
        val lottery = Lottery(numberOfWinners = 3)

        val winners = lottery.generateWinners()

        // Nobody joined, so there are no winners
        assertTrue(winners.isEmpty())
        assertTrue(lottery.winners.isEmpty())
    }

    @Test
    fun generateWinners_winnersComeFromWaitlist() {
        val e1 = Entrant(id = "E1")
        val e2 = Entrant(id = "E2")
        val e3 = Entrant(id = "E3")
        val lottery = Lottery(numberOfWinners = 2, waitlist = mutableListOf(e1, e2, e3))

        val winners = lottery.generateWinners()

        // Every winner must be someone who was on the waitlist
        for (winner in winners) {
            assertTrue(lottery.waitlist.contains(winner))
        }
    }

    @Test
    fun generateWinners_noDuplicateWinners() {
        val lottery = Lottery(
            numberOfWinners = 3,
            waitlist = mutableListOf(Entrant(id = "E1"), Entrant(id = "E2"), Entrant(id = "E3"))
        )

        val winners = lottery.generateWinners()

        // A set removes duplicates, so the sizes match only if nobody won twice
        assertEquals(winners.size, winners.toSet().size)
    }

    @Test
    fun generateWinners_updatesWinnersProperty() {
        val lottery = Lottery(
            numberOfWinners = 1,
            waitlist = mutableListOf(Entrant(id = "E1"), Entrant(id = "E2"))
        )

        val returned = lottery.generateWinners()

        // The returned list should also be saved on the lottery
        assertEquals(returned, lottery.winners)
    }

    @Test
    fun generateWinners_doesNotChangeWaitlist() {
        val e1 = Entrant(id = "E1")
        val e2 = Entrant(id = "E2")
        val e3 = Entrant(id = "E3")
        val lottery = Lottery(numberOfWinners = 2, waitlist = mutableListOf(e1, e2, e3))

        lottery.generateWinners()

        // The waitlist should stay the same (same people, same order)
        assertEquals(mutableListOf(e1, e2, e3), lottery.waitlist)
    }

    @Test
    fun waitlist_withNoCapacity_isNeverFull() {
        assertFalse(isWaitlistFull(count = 0, capacity = null))
        assertFalse(isWaitlistFull(count = 100_000, capacity = null))
    }

    @Test
    fun waitlist_belowCapacity_isNotFull() {
        assertFalse(isWaitlistFull(count = 0, capacity = 3))
        assertFalse(isWaitlistFull(count = 2, capacity = 3))
    }

    @Test
    fun waitlist_atCapacity_isFull() {
        assertTrue(isWaitlistFull(count = 3, capacity = 3))
    }

    @Test
    fun waitlist_overCapacity_isFull() {
        // Happens if the owner lowers the capacity below the current number of entrants when editing lottery
        assertTrue(isWaitlistFull(count = 5, capacity = 3))
    }

}