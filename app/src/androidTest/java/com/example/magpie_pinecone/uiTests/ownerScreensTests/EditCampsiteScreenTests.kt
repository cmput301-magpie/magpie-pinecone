package com.example.magpie_pinecone.uiTests.ownerScreensTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.frontend.ownerScreens.EditCampsiteScreen
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class EditCampsiteScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()   // Shows a screen on the emulator so we can test it

    // Remember what the screen sent back
    private var submittedCampsite: Campsite? = null
    private var backPressed = false

    /**
     * isEdit = false means "Create Campsite", true means "Edit Campsite".
     */
    private fun showEditCampsiteScreen(campsite: Campsite, isEdit: Boolean) {
        composeTestRule.setContent {
            MagpiepineconeTheme {
                EditCampsiteScreen(
                    ownerId = "abc123",
                    isEdit = isEdit,
                    campsite = campsite,
                    onBack = { backPressed = true },
                    onSubmit = { submittedCampsite = it }
                )
            }
        }
    }

    @Test
    fun newCampsite_showsCreateTitle() {
        showEditCampsiteScreen(Campsite(), isEdit = false)

        // A new campsite uses the "create" words
        composeTestRule.onNodeWithText("Create Campsite").assertIsDisplayed()
        composeTestRule.onNodeWithText("Register Campsite").assertIsDisplayed()
    }

    @Test
    fun existingCampsite_showsEditTitleAndCurrentValues() {
        val campsite = Campsite(name = "Gronk Campground", location = "1234 Rizz St")
        showEditCampsiteScreen(campsite, isEdit = true)

        // Editing uses the "edit" words
        composeTestRule.onNodeWithText("Edit Campsite").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save Campsite").assertIsDisplayed()

        // The boxes start with the campsite's current values
        composeTestRule.onNodeWithText("Name").assertTextContains("Gronk Campground")
        composeTestRule.onNodeWithText("Location").assertTextContains("1234 Rizz St")
    }

    @Test
    fun registerCampsite_fillsInCampsite() {
        showEditCampsiteScreen(Campsite(), isEdit = false)

        // Type into every box (each box is found by its label)
        composeTestRule.onNodeWithText("Name").performTextInput("Gronk Campground")
        composeTestRule.onNodeWithText("Location").performTextInput("1234 Rizz St")
        composeTestRule.onNodeWithText("Price ($)").performTextInput("38")
        composeTestRule.onNodeWithText("Description").performTextInput("Quiet spot by the river")
        composeTestRule.onNodeWithText("Max Occupancy").performTextInput("5")
        composeTestRule.onNodeWithText("Tags").performTextInput("River, Lake")

        composeTestRule.onNodeWithText("Register Campsite").performClick()

        // The campsite sent back should hold everything we typed
        assertNotNull(submittedCampsite)
        assertEquals("abc123", submittedCampsite!!.ownerId)
        assertEquals("Gronk Campground", submittedCampsite!!.name)
        assertEquals("1234 Rizz St", submittedCampsite!!.location)
        assertEquals(38.0, submittedCampsite!!.price, 0.0)
        assertEquals("Quiet spot by the river", submittedCampsite!!.description)
        assertEquals(5, submittedCampsite!!.maxOccupancy)

        // Tags are split on commas and the spaces are trimmed off
        assertEquals(listOf("River", "Lake"), submittedCampsite!!.tags)
    }

    @Test
    fun blankNumbers_becomeZero() {
        showEditCampsiteScreen(Campsite(), isEdit = false)

        // Only fill in the name, leave price and occupancy empty
        composeTestRule.onNodeWithText("Name").performTextInput("Gronk Campground")
        composeTestRule.onNodeWithText("Register Campsite").performClick()

        // Empty number boxes should not crash, they just save as 0
        assertEquals(0.0, submittedCampsite!!.price, 0.0)
        assertEquals(0, submittedCampsite!!.maxOccupancy)
        assertTrue(submittedCampsite!!.tags.isEmpty())
    }

    @Test
    fun backButton_callsOnBack() {
        showEditCampsiteScreen(Campsite(), isEdit = false)

        composeTestRule.onNodeWithContentDescription("Back Button").performClick()
        assertTrue(backPressed)
    }
}