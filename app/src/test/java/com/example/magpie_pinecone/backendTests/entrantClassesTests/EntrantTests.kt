package com.example.magpie_pinecone.backendTests.entrantClassesTests

import com.example.magpie_pinecone.backend.UserClasses.AccountType
import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.backend.entrantClasses.Entrant
import com.example.magpie_pinecone.backend.reservationClasses.Reservation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EntrantTests {

    @Test
    fun baseConstructors() {
        val entrant = Entrant()   // Firestore needs every class object to have base constructors

        assertEquals("", entrant.id)
        assertTrue(entrant.activeReservations.isEmpty())
        assertTrue(entrant.pastReservations.isEmpty())
        assertFalse(entrant.notifOptOut)
    }

    @Test
    fun valuedConstructors() {
        val entrant = Entrant(
            id = "abc123",
            name = "Baby Gronk",
            email = "Rizz@gmail.com",
            phoneNumber = "780-123-4567",
            notifOptOut = true
        )

        assertEquals("abc123", entrant.id)
        assertEquals("Baby Gronk", entrant.name)
        assertEquals("Rizz@gmail.com", entrant.email)
        assertEquals("780-123-4567", entrant.phoneNumber)
        assertTrue(entrant.notifOptOut)
    }

    @Test
    fun entrant_isUser() {
        val entrant: User = Entrant(id = "abc123")

        assertEquals("abc123", entrant.id)
        assertEquals(AccountType.Entrant, entrant.accountType)
    }

    @Test
    fun twoEntrants_doNotShareReservationLists() {
        val a = Entrant()
        val b = Entrant()

        // Changing list of a to see if b list also updates
        a.activeReservations.add(Reservation())

        assertTrue(b.activeReservations.isEmpty())
        assertTrue(b.pastReservations.isEmpty())
    }

    @Test
    fun entrant_inheritsUserDefaults() {
        val entrant = Entrant()

        // These fields come from the User class
        assertEquals("", entrant.name)
        assertEquals("", entrant.email)
        assertEquals("", entrant.phoneNumber)
        assertEquals(AccountType.Entrant, entrant.accountType)
        assertFalse(entrant.rulesAccepted)   // rules not accepted yet
    }

    @Test
    fun entrant_fieldsCanBeUpdated() {
        val entrant = Entrant()

        // Fields are "var", so the entrant can update their profile later
        entrant.id = "xyz789"
        entrant.name = "Livvy Dunne"
        entrant.email = "livvy@gmail.com"
        entrant.phoneNumber = "587-987-6543"

        assertEquals("xyz789", entrant.id)
        assertEquals("Livvy Dunne", entrant.name)
        assertEquals("livvy@gmail.com", entrant.email)
        assertEquals("587-987-6543", entrant.phoneNumber)
    }

    @Test
    @Suppress("KotlinConstantConditions")
    fun notifOptOut_canBeToggled() {
        val entrant = Entrant()

        // Starts as opted in (false)
        assertFalse(entrant.notifOptOut)

        // Entrant opts out of notifications
        entrant.notifOptOut = true
        assertTrue(entrant.notifOptOut)

        // Entrant opts back in
        entrant.notifOptOut = false
        assertFalse(entrant.notifOptOut)
    }

    @Test
    fun entrant_canAddReservation() {
        val entrant = Entrant(id = "abc123")
        val reservation = Reservation(ID = "reservation123", entrantID = "abc123")

        entrant.activeReservations.add(reservation)
    }
}