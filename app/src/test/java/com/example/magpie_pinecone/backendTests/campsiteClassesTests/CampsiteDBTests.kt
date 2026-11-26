package com.example.magpie_pinecone.backendTests.campsiteClassesTests

import com.example.magpie_pinecone.backend.campsiteClasses.hasAvailableDateRange
import com.example.magpie_pinecone.backend.campsiteClasses.localDateToUtcMillis
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class CampsiteDBTests {

    private fun date(day: Int, hour: Int = 0): Date {
        return Calendar.getInstance().apply {
            clear()
            set(2027, Calendar.JUNE, day, hour, 0)
        }.time
    }

    // Sample available dates from June 5 to June 10
    private val availableDays = (5..10).map {
        localDateToUtcMillis(date(it))
    }.toSet()

    @Test
    fun dateRange_allDatesAvailable() {
        val start = localDateToUtcMillis(date(5))
        val end = localDateToUtcMillis(date(10))

        assertTrue(hasAvailableDateRange(availableDays, start, end))
    }

    @Test
    fun dateRange_missingDate() {
        val start = localDateToUtcMillis(date(5))
        val end = localDateToUtcMillis(date(10))

        // The campsite must be available on every day, not just the endpoints
        val dates = availableDays - localDateToUtcMillis(date(7))

        assertFalse(hasAvailableDateRange(dates, start, end))
    }

    @Test
    fun dateRange_outsideAvailableDates() {
        val start = localDateToUtcMillis(date(5))
        val end = localDateToUtcMillis(date(11))

        assertFalse(hasAvailableDateRange(availableDays, start, end))
    }

    @Test
    fun dateRange_invalidRange() {
        val start = localDateToUtcMillis(date(10))
        val end = localDateToUtcMillis(date(5))

        assertFalse(hasAvailableDateRange(availableDays, start, end))
    }

    @Test
    fun dateConversion_preservesCalendarDay() {
        val morning = date(5, 8)
        val evening = date(5, 20)

        val expected = Calendar.getInstance(
            TimeZone.getTimeZone("UTC")
        ).apply {
            clear()
            set(2027, Calendar.JUNE, 5)
        }.timeInMillis

        // Different times on the same day should represent the same calendar date
        assertEquals(expected, localDateToUtcMillis(morning))
        assertEquals(expected, localDateToUtcMillis(evening))

        assertTrue(hasAvailableDateRange(availableDays, expected, expected))
    }
}