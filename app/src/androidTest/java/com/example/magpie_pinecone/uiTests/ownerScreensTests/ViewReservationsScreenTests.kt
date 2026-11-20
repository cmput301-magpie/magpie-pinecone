package com.example.magpie_pinecone.uiTests.ownerScreensTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.magpie_pinecone.backend.campsiteClasses.CampsiteDB
import com.example.magpie_pinecone.frontend.ownerScreens.ViewReservationsScreen
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ViewReservationsScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()   // Shows a screen on the emulator so we can test it

    private var backPressed = false

    /**
     * We always pass an empty list, so CampsiteDB is never asked to read anything.
     */
    private fun showViewReservationsScreen() {
        composeTestRule.setContent {
            MagpiepineconeTheme {
                ViewReservationsScreen(
                    activeReservations = emptyList(),
                    campsiteDB = CampsiteDB(),
                    onBack = { backPressed = true }
                )
            }
        }
    }

    @Test
    fun showsTitleAndThreeTabs() {
        showViewReservationsScreen()

        composeTestRule.onNodeWithText("Reservations").assertIsDisplayed()
        composeTestRule.onNodeWithText("Active").assertIsDisplayed()
        composeTestRule.onNodeWithText("Past").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancelled").assertIsDisplayed()
    }

    @Test
    fun switchingTabs_changesTheList() {
        showViewReservationsScreen()

        // No active reservations, so no rows yet
        composeTestRule.onNodeWithText("View Details").assertDoesNotExist()

        // The Past tab shows its row
        composeTestRule.onNodeWithText("Past").performClick()
        composeTestRule.onNodeWithText("View Details").assertIsDisplayed()

        // So does the Cancelled tab
        composeTestRule.onNodeWithText("Cancelled").performClick()
        composeTestRule.onNodeWithText("View Details").assertIsDisplayed()
    }

    @Test
    fun backButton_callsOnBack() {
        showViewReservationsScreen()

        composeTestRule.onNodeWithContentDescription("Back Button").performClick()
        assertTrue(backPressed)
    }
}