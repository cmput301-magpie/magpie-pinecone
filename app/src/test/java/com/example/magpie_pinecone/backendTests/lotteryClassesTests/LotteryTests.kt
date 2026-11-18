package com.example.magpie_pinecone.backendTests.lotteryClassesTests

import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.entrantClasses.Entrant
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import org.junit.Assert.assertEquals
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
        assertEquals(100, lottery.waitlistCapacity)
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
}