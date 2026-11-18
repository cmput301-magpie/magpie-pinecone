package com.example.magpie_pinecone.backendTests.ownerClassesTests

import com.example.magpie_pinecone.backend.UserClasses.AccountType
import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.backend.ownerClasses.Owner
import com.example.magpie_pinecone.backend.reservationClasses.Reservation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OwnerTests {

    @Test
    fun baseConstructors() {
        val owner = Owner()   // Firestore needs every class object to have base constructors

        assertEquals("", owner.id)
        assertTrue(owner.campsites.isEmpty())
        assertTrue(owner.lotteries.isEmpty())
        assertTrue(owner.activeReservations.isEmpty())
        assertTrue(owner.cancelledReservations.isEmpty())
        assertTrue(owner.pastReservations.isEmpty())
        assertTrue(owner.sentNotifications.isEmpty())
    }

    @Test
    fun valuedConstructors() {
        val campsite = Campsite()
        val lottery = Lottery()
        val active = Reservation()
        val cancelled = Reservation()
        val past = Reservation()

        val owner = Owner(
            id = "abc123",
            name = "Baby Gronk",
            email = "Rizz@gmail.com",
            phoneNumber = "780-123-4567",
            campsites = mutableListOf(campsite),
            lotteries = mutableListOf(lottery),
            activeReservations = mutableListOf(active),
            cancelledReservations = mutableListOf(cancelled),
            pastReservations = mutableListOf(past),
            sentNotifications = mutableListOf()
        )

        assertEquals("abc123", owner.id)
        assertEquals("Baby Gronk", owner.name)
        assertEquals("Rizz@gmail.com", owner.email)
        assertEquals("780-123-4567", owner.phoneNumber)

        assertEquals(mutableListOf(campsite), owner.campsites)
        assertEquals(mutableListOf(lottery), owner.lotteries)
        assertEquals(mutableListOf(active), owner.activeReservations)
        assertEquals(mutableListOf(cancelled), owner.cancelledReservations)
        assertEquals(mutableListOf(past), owner.pastReservations)
    }

    @Test
    fun owner_isUser() {
        val owner: User = Owner(id = "abc123")

        assertEquals("abc123", owner.id)
        assertEquals(AccountType.Owner, owner.accountType)
    }

    @Test
    fun twoOwners_doNotShareLists() {
        val a = Owner()
        val b = Owner()

        // Changing lists of a to see if b lists also update
        a.campsites.add(Campsite())
        a.activeReservations.add(Reservation())

        assertTrue(b.campsites.isEmpty())
        assertTrue(b.activeReservations.isEmpty())
    }

    @Test
    fun owner_inheritsUserDefaults() {
        val owner = Owner()

        // These fields come from the User class
        assertEquals("", owner.name)
        assertEquals("", owner.email)
        assertEquals("", owner.phoneNumber)
        assertEquals(AccountType.Owner, owner.accountType)   // always Owner, not the User default
        assertTrue(!owner.rulesAccepted)                     // rules not accepted yet
    }

    @Test
    fun owner_canAddAndRemoveCampsites() {
        val owner = Owner(id = "abc123")
        val campsite1 = Campsite(name = "Lakeview")
        val campsite2 = Campsite(name = "Hilltop")

        // Owner registers two campsites
        owner.campsites.add(campsite1)
        owner.campsites.add(campsite2)
        assertEquals(2, owner.campsites.size)

        // Owner removes one campsite
        owner.campsites.remove(campsite1)
        assertEquals(1, owner.campsites.size)
        assertEquals(campsite2, owner.campsites[0])
    }

    @Test
    fun owner_canAddLottery() {
        val owner = Owner(id = "abc123")
        val lottery = Lottery(id = "lottery123", ownerId = "abc123")

        owner.lotteries.add(lottery)

        assertEquals(1, owner.lotteries.size)
        assertEquals("abc123", owner.lotteries[0].ownerId)   // lottery belongs to this owner
    }

    @Test
    fun owner_reservationCanMoveToCancelled() {
        val reservation = Reservation(ID = "reservation123")
        val owner = Owner(activeReservations = mutableListOf(reservation))

        // Move the reservation from active to cancelled
        owner.activeReservations.remove(reservation)
        owner.cancelledReservations.add(reservation)

        assertTrue(owner.activeReservations.isEmpty())
        assertEquals(mutableListOf(reservation), owner.cancelledReservations)
    }

    @Test
    fun twoOwners_doNotShareOtherLists() {
        val a = Owner()
        val b = Owner()

        // The first test checked campsites and activeReservations, so this checks the rest
        a.lotteries.add(Lottery())
        a.cancelledReservations.add(Reservation())
        a.pastReservations.add(Reservation())

        assertTrue(b.lotteries.isEmpty())
        assertTrue(b.cancelledReservations.isEmpty())
        assertTrue(b.pastReservations.isEmpty())
    }
}
