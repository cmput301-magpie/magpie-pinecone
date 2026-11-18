package com.example.magpie_pinecone.backendTests.reservationClasssesTests

import com.example.magpie_pinecone.backend.reservationClasses.Reservation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Date

class ReservationTests {

    @Test
    fun baseConstructors() {
        val before = Date()
        val reservation = Reservation()   // Firestore needs every class object to have base constructors
        val after = Date()

        assertEquals("", reservation.ID)
        assertEquals("", reservation.entrantID)
        assertEquals("", reservation.ownerID)
        assertEquals("", reservation.campsiteID)
        assertEquals(0, reservation.numGuests)
        assertFalse(reservation.termsAndConditions)
        assertFalse(reservation.isCancelled)

        // Default dates are "now", so they should fall between before and after
        assertFalse(reservation.startDate.before(before))
        assertFalse(reservation.startDate.after(after))
        assertFalse(reservation.endDate.before(before))
        assertFalse(reservation.endDate.after(after))
    }

    @Test
    fun valuedConstructors() {
        val start = Date(1_800_000_000_000)   // a fixed date so the test always gives the same result
        val end = Date(1_800_259_200_000)     // 3 days after start

        val reservation = Reservation(
            ID = "reservation123",
            entrantID = "abc123",
            ownerID = "owner123",
            campsiteID = "campsite123",
            startDate = start,
            endDate = end,
            numGuests = 4,
            termsAndConditions = true,
            isCancelled = true
        )

        assertEquals("reservation123", reservation.ID)
        assertEquals("abc123", reservation.entrantID)
        assertEquals("owner123", reservation.ownerID)
        assertEquals("campsite123", reservation.campsiteID)
        assertEquals(start, reservation.startDate)
        assertEquals(end, reservation.endDate)
        assertEquals(4, reservation.numGuests)
        assertTrue(reservation.termsAndConditions)
        assertTrue(reservation.isCancelled)
    }

    @Test
    fun reservation_fieldsCanBeUpdated() {
        val reservation = Reservation()
        val newStart = Date(1_800_000_000_000)
        val newEnd = Date(1_800_259_200_000)

        // All fields are "var", so we should be able to change them after creating the reservation
        reservation.ID = "xyz789"
        reservation.entrantID = "entrant789"
        reservation.ownerID = "owner789"
        reservation.campsiteID = "campsite789"
        reservation.startDate = newStart
        reservation.endDate = newEnd
        reservation.numGuests = 6

        assertEquals("xyz789", reservation.ID)
        assertEquals("entrant789", reservation.entrantID)
        assertEquals("owner789", reservation.ownerID)
        assertEquals("campsite789", reservation.campsiteID)
        assertEquals(newStart, reservation.startDate)
        assertEquals(newEnd, reservation.endDate)
        assertEquals(6, reservation.numGuests)
    }

    @Test
    @Suppress("KotlinConstantConditions")
    fun reservation_canBeCancelled() {
        val reservation = Reservation()

        // Starts as not cancelled
        assertFalse(reservation.isCancelled)

        // Owner or entrant cancels the reservation
        reservation.isCancelled = true
        assertTrue(reservation.isCancelled)
    }

    @Test
    @Suppress("KotlinConstantConditions")
    fun termsAndConditions_canBeAccepted() {
        val reservation = Reservation()

        // Starts as not accepted
        assertFalse(reservation.termsAndConditions)

        // Entrant accepts the terms
        reservation.termsAndConditions = true
        assertTrue(reservation.termsAndConditions)
    }

    @Test
    fun twoReservations_doNotShareData() {
        val a = Reservation(ID = "a")
        val b = Reservation(ID = "b")

        // Changing a should not change b
        a.numGuests = 10
        a.isCancelled = true
        a.startDate.time = 0   // changes a's date object directly

        assertEquals(0, b.numGuests)
        assertFalse(b.isCancelled)
        assertNotEquals(0L, b.startDate.time)   // b has its own date object
        assertNotEquals(a.ID, b.ID)
    }
}