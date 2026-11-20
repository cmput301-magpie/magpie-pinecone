package com.example.magpie_pinecone.uiTests.ownerScreensTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.frontend.ownerScreens.ViewCampsiteScreen
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ViewCampsiteScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()   // Shows a screen on the emulator so we can test it

    private val campsite = Campsite(
        ID = "site123",
        name = "Gronk Campground",
        location = "1234 Rizz St",
        maxOccupancy = 5,
        description = "Quiet spot by the river",
        tags = listOf("River", "Lake")
    )

    // Remember what was pressed
    private var backPressed = false
    private var editPressed = false

    private fun showViewCampsiteScreen() {
        composeTestRule.setContent {
            MagpiepineconeTheme {
                ViewCampsiteScreen(
                    campsite = campsite,
                    onBack = { backPressed = true },
                    onEdit = { editPressed = true }
                )
            }
        }
    }

    @Test
    fun showsCampsiteInfo() {
        showViewCampsiteScreen()

        // Every piece of campsite info should be on screen
        composeTestRule.onNodeWithText("View Campsite").assertIsDisplayed()
        composeTestRule.onNodeWithText("Gronk Campground").assertIsDisplayed()
        composeTestRule.onNodeWithText("1234 Rizz St").assertIsDisplayed()
        composeTestRule.onNodeWithText("Quiet spot by the river").assertIsDisplayed()
        composeTestRule.onNodeWithText("Max Occupancy: 5").assertIsDisplayed()

        // Tags are joined with commas
        composeTestRule.onNodeWithText("Tags: River, Lake").assertIsDisplayed()
    }

    @Test
    fun editButton_callsOnEdit() {
        showViewCampsiteScreen()

        composeTestRule.onNodeWithText("Edit Campsite").performClick()
        assertTrue(editPressed)
    }

    @Test
    fun backButton_callsOnBack() {
        showViewCampsiteScreen()

        composeTestRule.onNodeWithContentDescription("Back Button").performClick()
        assertTrue(backPressed)
    }
}