package com.example.magpie_pinecone.backendTests.reviewClassesTests

import com.example.magpie_pinecone.backend.reviewClasses.averageRating
import com.example.magpie_pinecone.backend.reviewClasses.reviewDocumentId
import com.example.magpie_pinecone.backend.reviewClasses.updatedRatingCount
import com.example.magpie_pinecone.backend.reviewClasses.updatedRatingTotal
import org.junit.Assert.assertEquals
import org.junit.Test

class ReviewDBTests {

    @Test
    fun documentId_combinesCampsiteAndEntrant() {
        assertEquals("campsite123_entrant456", reviewDocumentId("campsite123", "entrant456"))
    }

    @Test
    fun documentId_isSameForSameEntrantAndCampsite() {
        // Posting a review again must target the same document, so it edits instead of duplicating
        assertEquals(
            reviewDocumentId("campsite123", "entrant456"),
            reviewDocumentId("campsite123", "entrant456")
        )
    }

    @Test
    fun total_firstReview_isItsRating() {
        assertEquals(5, updatedRatingTotal(total = 0, newRating = 5, oldRating = null))
    }

    @Test
    fun total_newReview_isAddedToExistingTotal() {
        assertEquals(9, updatedRatingTotal(total = 5, newRating = 4, oldRating = null))
    }

    @Test
    fun total_editedReview_replacesOldRating() {
        // 5 + 3 = 8; the 3-star reviewer changes their rating to 5
        assertEquals(10, updatedRatingTotal(total = 8, newRating = 5, oldRating = 3))
    }

    @Test
    fun total_editedReview_withSameRating_changesNothing() {
        assertEquals(9, updatedRatingTotal(total = 9, newRating = 4, oldRating = 4))
    }

    @Test
    fun count_newReview_raisesCount() {
        assertEquals(1, updatedRatingCount(count = 0, isNewReview = true))
        assertEquals(3, updatedRatingCount(count = 2, isNewReview = true))
    }

    @Test
    fun count_editedReview_keepsCount() {
        assertEquals(2, updatedRatingCount(count = 2, isNewReview = false))
    }

    @Test
    fun average_withNoReviews_isZero() {
        // Avoids dividing by zero for a campsite nobody has reviewed
        assertEquals(0.0, averageRating(total = 0, count = 0), 0.0)
    }

    @Test
    fun average_isTotalDividedByCount() {
        assertEquals(4.5, averageRating(total = 9, count = 2), 0.0)
    }

    @Test
    fun average_followsReviewsBeingPostedAndEdited() {
        // Same steps submitReview takes, starting from a campsite with no reviews
        var total = 0
        var count = 0

        // First entrant posts 5 stars
        total = updatedRatingTotal(total, newRating = 5, oldRating = null)
        count = updatedRatingCount(count, isNewReview = true)
        assertEquals(5.0, averageRating(total, count), 0.0)

        // Second entrant posts 4 stars
        total = updatedRatingTotal(total, newRating = 4, oldRating = null)
        count = updatedRatingCount(count, isNewReview = true)
        assertEquals(4.5, averageRating(total, count), 0.0)

        // Second entrant edits their review to 2 stars
        total = updatedRatingTotal(total, newRating = 2, oldRating = 4)
        count = updatedRatingCount(count, isNewReview = false)
        assertEquals(3.5, averageRating(total, count), 0.0)
        assertEquals(2, count)
    }
}