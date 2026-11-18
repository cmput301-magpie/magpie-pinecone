package com.example.magpie_pinecone.backendTests.campsiteClassesTests

import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.google.firebase.firestore.GeoPoint
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Date

class CampsiteTests {

    @Test
    fun baseConstructors() {
        val campsite = Campsite()   // Firestore needs every class object to have base constructors

        assertEquals("", campsite.ID)
        assertEquals("", campsite.name)
        assertEquals("", campsite.location)
        assertEquals(0, campsite.maxOccupancy)
        assertEquals(0.0, campsite.price, 0.0)    // last number is the allowed difference for doubles
        assertEquals(0.0, campsite.rating, 0.0)
        assertNull(campsite.imageUrl)
        assertEquals("", campsite.rulesAndPolicy)
        assertTrue(campsite.availableDates.isEmpty())
        assertTrue(campsite.bookedDates.isEmpty())
        assertEquals("", campsite.ownerId)
        assertEquals("", campsite.description)
        assertNull(campsite.mapLocation)
        assertTrue(campsite.tags.isEmpty())
    }

    @Test
    fun valuedConstructors() {
        val day1 = Date(1_800_000_000_000)   // fixed dates so the test always gives the same result
        val day2 = Date(1_800_086_400_000)   // 1 day after day1
        val location = GeoPoint(53.5461, -113.4938)   // Edmonton

        val campsite = Campsite(
            ID = "campsite123",
            name = "Lakeview",
            location = "Jasper, AB",
            maxOccupancy = 6,
            price = 45.50,
            rating = 4.5,
            imageUrl = "https://example.com/lakeview.jpg",
            rulesAndPolicy = "No campfires after 11pm",
            availableDates = listOf(day1),
            bookedDates = listOf(day2),
            ownerId = "abc123",
            description = "Quiet spot by the lake",
            mapLocation = location,
            tags = listOf("lake", "pet friendly")
        )

        assertEquals("campsite123", campsite.ID)
        assertEquals("Lakeview", campsite.name)
        assertEquals("Jasper, AB", campsite.location)
        assertEquals(6, campsite.maxOccupancy)
        assertEquals(45.50, campsite.price, 0.0)
        assertEquals(4.5, campsite.rating, 0.0)
        assertEquals("https://example.com/lakeview.jpg", campsite.imageUrl)
        assertEquals("No campfires after 11pm", campsite.rulesAndPolicy)
        assertEquals(listOf(day1), campsite.availableDates)
        assertEquals(listOf(day2), campsite.bookedDates)
        assertEquals("abc123", campsite.ownerId)
        assertEquals("Quiet spot by the lake", campsite.description)
        assertEquals(location, campsite.mapLocation)
        assertEquals(listOf("lake", "pet friendly"), campsite.tags)
    }

    @Test
    fun campsite_fieldsCanBeUpdated() {
        val campsite = Campsite()

        // All fields are "var", so the owner can update campsite details later
        campsite.ID = "xyz789"
        campsite.name = "Hilltop"
        campsite.location = "Banff, AB"
        campsite.maxOccupancy = 4
        campsite.price = 30.0
        campsite.rating = 3.8
        campsite.imageUrl = "https://example.com/hilltop.jpg"
        campsite.rulesAndPolicy = "No pets"
        campsite.ownerId = "owner789"
        campsite.description = "Great view"
        campsite.tags = listOf("mountain")

        assertEquals("xyz789", campsite.ID)
        assertEquals("Hilltop", campsite.name)
        assertEquals("Banff, AB", campsite.location)
        assertEquals(4, campsite.maxOccupancy)
        assertEquals(30.0, campsite.price, 0.0)
        assertEquals(3.8, campsite.rating, 0.0)
        assertEquals("https://example.com/hilltop.jpg", campsite.imageUrl)
        assertEquals("No pets", campsite.rulesAndPolicy)
        assertEquals("owner789", campsite.ownerId)
        assertEquals("Great view", campsite.description)
        assertEquals(listOf("mountain"), campsite.tags)
    }

    @Test
    fun campsite_dateCanMoveToBooked() {
        val day1 = Date(1_800_000_000_000)
        val day2 = Date(1_800_086_400_000)
        val campsite = Campsite(availableDates = listOf(day1, day2))

        // Someone books day1, so it moves from available to booked
        campsite.availableDates = campsite.availableDates - day1
        campsite.bookedDates = campsite.bookedDates + day1

        assertEquals(listOf(day2), campsite.availableDates)
        assertEquals(listOf(day1), campsite.bookedDates)
    }

    @Test
    fun twoCampsites_doNotShareData() {
        val a = Campsite(ID = "a")
        val b = Campsite(ID = "b")

        // Changing a should not change b
        a.name = "Changed Name"
        a.tags = listOf("forest")
        a.availableDates = listOf(Date())

        assertEquals("", b.name)
        assertTrue(b.tags.isEmpty())
        assertTrue(b.availableDates.isEmpty())
    }
}