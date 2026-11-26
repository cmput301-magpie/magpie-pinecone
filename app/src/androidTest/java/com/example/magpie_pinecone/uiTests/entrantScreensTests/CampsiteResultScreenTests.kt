package com.example.magpie_pinecone.uiTests.entrantScreensTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.frontend.entrantScreens.CampsiteResultScreen
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CampsiteResultScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()   // Shows a screen on the emulator so we can test it

    // Same sample campsite as the other tests
    private val campsite = Campsite(
        ID = "site123",
        name = "Gronk Campground",
        location = "1234 Rizz St",
        maxOccupancy = 5,
        description = "Quiet spot by the river",
        tags = listOf("River", "Lake")
    )

    private var backPressed = false

    private fun showResultScreen(campsite: Campsite? = null) {
        composeTestRule.setContent {
            MagpiepineconeTheme {
                CampsiteResultScreen(
                    onBack = { backPressed = true; true },
                    title = "Campsite Reserved",
                    message = "Your reservation was placed successfully!",
                    campsite = campsite
                )
            }
        }
    }

    @Test
    fun showsTitleAndMessage() {
        showResultScreen()

        // The title and message passed in should be shown
        composeTestRule.onNodeWithText("Campsite Reserved").assertIsDisplayed()
        composeTestRule.onNodeWithText("Your reservation was placed successfully!").assertIsDisplayed()
    }

    @Test
    fun showsCampsiteDetails() {
        showResultScreen(campsite)

        // The real campsite's details should be listed
        composeTestRule.onNodeWithText("Gronk Campground").assertIsDisplayed()
        composeTestRule.onNodeWithText("1234 Rizz St").assertIsDisplayed()

        // These are further down, so scroll to them first
        composeTestRule.onNodeWithText("Quiet spot by the river").performScrollTo().assertIsDisplayed()

        // Tags are joined with commas
        composeTestRule.onNodeWithText("River, Lake").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun noCampsite_hidesDetails() {
        showResultScreen(null)

        // Without a campsite there are no fake details anymore
        composeTestRule.onNodeWithText("Name").assertDoesNotExist()
        composeTestRule.onNodeWithText("Max Occupancy").assertDoesNotExist()
    }

    @Test
    fun showsDownloadCsvButton() {
        showResultScreen()

        composeTestRule.onNodeWithText("Download CSV").assertIsDisplayed()
    }

    @Test
    fun backButton_callsOnBack() {
        showResultScreen()

        // Uses the shared back button now
        composeTestRule.onNodeWithContentDescription("Back Button").performClick()
        assertTrue(backPressed)
    }
}