package com.example.magpie_pinecone.uiTests.ownerScreensTests

import android.os.Build
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.filters.SdkSuppress
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.reservationClasses.Reservation
import com.example.magpie_pinecone.frontend.ownerScreens.DatesBookedScreen
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

@SdkSuppress(minSdkVersion = Build.VERSION_CODES.O)   // Same as the screen: it uses java.time dates
class DatesBookedScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()   // Shows a screen on the emulator so we can test it

    private val campsite = Campsite(ID = "site123", name = "Gronk Campground")

    // One reservation for today, booked by our sample entrant
    private val reservation = Reservation(
        ID = "res1",
        entrantID = "abc123",
        campsiteID = "site123",
        startDate = Date(),
        endDate = Date()
    )

    private var backPressed = false

    private fun showDatesBookedScreen(reservations: List<Reservation>) {
        composeTestRule.setContent {
            MagpiepineconeTheme {
                DatesBookedScreen(
                    campsite = campsite,
                    reservations = reservations,
                    onBack = { backPressed = true }
                )
            }
        }
    }

    /** Builds the month header text the screen shows, e.g. "November 2026". */
    private fun monthText(month: YearMonth): String =
        "${month.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${month.year}"

    @Test
    fun showsTitleAndCampsiteName() {
        showDatesBookedScreen(listOf(reservation))

        composeTestRule.onNodeWithText("Dates Booked").assertIsDisplayed()
        composeTestRule.onNodeWithText("Gronk Campground").assertIsDisplayed()

        // With a booking, the "no bookings" message should not show
        composeTestRule.onNodeWithText("No upcoming bookings for this campsite.").assertDoesNotExist()
    }

    @Test
    fun noReservations_showsMessage() {
        showDatesBookedScreen(emptyList())

        composeTestRule.onNodeWithText("No upcoming bookings for this campsite.").assertIsDisplayed()
    }

    @Test
    fun calendarStartsOnThisMonth() {
        showDatesBookedScreen(emptyList())

        composeTestRule.onNodeWithText(monthText(YearMonth.now())).assertIsDisplayed()
    }

    @Test
    fun arrows_changeTheMonth() {
        showDatesBookedScreen(emptyList())
        val thisMonth = YearMonth.now()

        // The right arrow goes forward one month
        composeTestRule.onNodeWithContentDescription("Next month").performClick()
        composeTestRule.onNodeWithText(monthText(thisMonth.plusMonths(1))).assertIsDisplayed()

        // The left arrow twice goes back to last month
        composeTestRule.onNodeWithContentDescription("Previous month").performClick()
        composeTestRule.onNodeWithContentDescription("Previous month").performClick()
        composeTestRule.onNodeWithText(monthText(thisMonth.minusMonths(1))).assertIsDisplayed()
    }

    @Test
    fun backButton_callsOnBack() {
        showDatesBookedScreen(emptyList())

        composeTestRule.onNodeWithContentDescription("Back Button").performClick()
        assertTrue(backPressed)
    }
}