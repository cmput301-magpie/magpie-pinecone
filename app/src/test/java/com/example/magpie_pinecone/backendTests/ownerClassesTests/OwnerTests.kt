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
}