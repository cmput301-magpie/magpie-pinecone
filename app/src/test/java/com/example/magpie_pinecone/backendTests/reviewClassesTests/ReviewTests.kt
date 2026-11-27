package com.example.magpie_pinecone.backendTests.reviewClassesTests

import com.example.magpie_pinecone.backend.reviewClasses.Review
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.util.Date

class ReviewTests {

    @Test
    fun baseConstructors() {
        val before = Date()
        val review = Review()
        val after = Date()

        assertEquals("", review.ID)
        assertEquals("", review.campsiteID)
        assertEquals("", review.entrantID)
        assertEquals("", review.entrantName)
        assertEquals(0, review.rating)
        assertEquals("", review.comment)

        // Default dates are "now", so they should fall between before and after
        assertFalse(review.createdAt.before(before))
        assertFalse(review.createdAt.after(after))
    }

    @Test
    fun valuedConstructors() {
        val testDate = Date(1_800_000_000_000)

        val review = Review(
            ID = "campsiteID_entrantID",
            campsiteID = "campsiteID",
            entrantID = "entrantID",
            entrantName = "John Doe",
            rating = 3,
            comment = "Not bad, but not the best either...",
            createdAt = testDate
        )

        assertEquals("campsiteID_entrantID", review.ID)
        assertEquals("campsiteID", review.campsiteID)
        assertEquals("entrantID", review.entrantID)
        assertEquals("John Doe", review.entrantName)
        assertEquals(3, review.rating)
        assertEquals("Not bad, but not the best either...", review.comment)
        assertEquals(testDate, review.createdAt)
    }

    @Test
    fun review_fieldsCanBeUpdated() {
        val review = Review(rating = 2, comment = "Too muddy")
        val newDate = Date(1_800_000_000_000)

        review.ID = "xyz789"
        review.campsiteID = "campsite789"
        review.entrantID = "entrant789"
        review.entrantName = "Jane Doe"
        review.rating = 5
        review.comment = "Dried out, much better!"
        review.createdAt = newDate

        assertEquals("xyz789", review.ID)
        assertEquals("campsite789", review.campsiteID)
        assertEquals("entrant789", review.entrantID)
        assertEquals("Jane Doe", review.entrantName)
        assertEquals(5, review.rating)
        assertEquals("Dried out, much better!", review.comment)
        assertEquals(newDate, review.createdAt)
    }

    @Test
    fun twoReviews_doNotShareData() {
        val a = Review(ID = "a")
        val b = Review(ID = "b")

        // Changing a should not change b
        a.rating = 5
        a.comment = "Changed comment"
        a.createdAt.time = 0   // changes a's date object directly

        assertEquals(0, b.rating)
        assertEquals("", b.comment)
        assertNotEquals(0L, b.createdAt.time)   // b has its own date object
        assertNotEquals(a.ID, b.ID)
    }
}