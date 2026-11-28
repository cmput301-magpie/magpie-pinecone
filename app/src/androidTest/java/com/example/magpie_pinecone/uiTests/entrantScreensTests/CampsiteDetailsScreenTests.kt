package com.example.magpie_pinecone.uiTests.entrantScreensTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.frontend.entrantScreens.CampsiteDetailsScreen
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.performScrollTo
import org.junit.Assert.assertFalse

class CampsiteDetailsScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()   // Shows a screen on the emulator so we can test it

    private val campsite = Campsite(
        ID = "site123",
        name = "Gronk Campground",
        location = "1234 Rizz St",
        maxOccupancy = 5,
        price = 38.0,
        rating = 5.0,
        description = "Quiet spot by the river",
        tags = listOf("River")
    )

    // Remember what was pressed
    private var backPressed = false
    private var enteredLottery = false
    private var reservePressed = false

    private fun showDetailsScreen(isWinner: Boolean = false, waitlistFull: Boolean = false) {
        composeTestRule.setContent {
            MagpiepineconeTheme {
                CampsiteDetailsScreen(
                    campsite = campsite,
                    onBack = { backPressed = true },
                    onSubmit = { enteredLottery = true },
                    isWinner = isWinner,
                    onReserve = { reservePressed = true },
                    waitlistFull = waitlistFull
                )
            }
        }
    }

    @Test
    fun showsCampsiteInfo() {
        showDetailsScreen()

        // Title, description, location and tags come from the campsite
        composeTestRule.onNodeWithText("Gronk Campground").assertIsDisplayed()
        composeTestRule.onNodeWithText("Quiet spot by the river").assertIsDisplayed()
        composeTestRule.onNodeWithText("1234 Rizz St").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("River").assertIsDisplayed()
    }

    @Test
    fun enterLottery_callsOnSubmit() {
        showDetailsScreen()

        composeTestRule.onNodeWithText("Enter lottery").performClick()
        assertTrue(enteredLottery)
    }

    @Test
    fun notWinner_hidesReserve() {
        showDetailsScreen(isWinner = false)

        // Only lottery winners can reserve
        composeTestRule.onNodeWithText("Reserve").assertDoesNotExist()
    }

    @Test
    fun winner_reserveCallsOnReserve() {
        showDetailsScreen(isWinner = true)

        composeTestRule.onNodeWithText("Reserve").performClick()
        assertTrue(reservePressed)
    }

    @Test
    fun backButton_callsOnBack() {
        showDetailsScreen()

        composeTestRule.onNodeWithContentDescription("Back Button").performClick()
        assertTrue(backPressed)
    }

    @Test
    fun waitlistNotFull_enterLotteryIsEnabled() {
        showDetailsScreen()

        composeTestRule.onNodeWithText("Enter lottery").assertIsEnabled()
    }

    @Test
    fun waitlistFull_showsDisabledWaitlistFull() {
        showDetailsScreen(waitlistFull = true)

        // The enter button is replaced and cannot be pressed
        composeTestRule.onNodeWithText("Waitlist full").assertIsDisplayed().assertIsNotEnabled()
        composeTestRule.onNodeWithText("Enter lottery").assertDoesNotExist()

        composeTestRule.onNodeWithText("Waitlist full").performClick()
        assertFalse(enteredLottery)
    }
}