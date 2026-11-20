package com.example.magpie_pinecone.uiTests.ownerScreensTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.frontend.ownerScreens.EditLotteryScreen
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class EditLotteryScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()   // Shows a screen on the emulator so we can test it

    private val campsite = Campsite(ID = "site123", name = "Gronk Campground")

    // Remember what the screen sent back
    private var submittedLottery: Lottery? = null

    private fun showEditLotteryScreen(
        lottery: Lottery,
        isEdit: Boolean,
        availableCampsites: List<Campsite> = listOf(campsite)
    ) {
        composeTestRule.setContent {
            MagpiepineconeTheme {
                EditLotteryScreen(
                    ownerId = "abc123",
                    isEdit = isEdit,
                    lottery = lottery,
                    availableCampsites = availableCampsites,
                    onBack = {},
                    onSubmit = { submittedLottery = it }
                )
            }
        }
    }

    /**
     * Types a date into one of the Start/End boxes.
     * index 0 = Registration, 1 = Booking, 2 = Active.
     * The screen scrolls, so we scroll to the box first.
     */
    private fun typeDate(label: String, index: Int, date: String) {
        composeTestRule.onAllNodesWithText(label)[index]
            .performScrollTo()
            .performTextInput(date)
    }

    /** The submit button is the clickable node with this text (the title has the same words). */
    private fun submitButton(text: String) =
        composeTestRule.onNode(hasText(text) and hasClickAction())

    @Test
    fun newLottery_showsStartLottery() {
        showEditLotteryScreen(Lottery(), isEdit = false)

        // Title and button both say "Start Lottery"
        composeTestRule.onAllNodesWithText("Start Lottery")[0].assertIsDisplayed()
        composeTestRule.onNodeWithText("Gronk Campground").assertIsDisplayed()
    }

    @Test
    fun noCampsites_showsRegisterFirstMessage() {
        showEditLotteryScreen(Lottery(), isEdit = false, availableCampsites = emptyList())

        composeTestRule
            .onNodeWithText("You have no campsites yet. Register a campsite first.")
            .assertIsDisplayed()
    }

    @Test
    fun emptyForm_cannotSubmit() {
        showEditLotteryScreen(Lottery(), isEdit = false)

        // No campsite picked and no dates yet, so the button is off
        submitButton("Start Lottery").performScrollTo().assertIsNotEnabled()
    }

    @Test
    fun endBeforeStart_showsError() {
        showEditLotteryScreen(Lottery(), isEdit = false)

        // Registration ends before it starts
        typeDate("Start", 0, "02/10/2027")
        typeDate("End", 0, "02/01/2027")

        // The end box warns us and the button stays off
        composeTestRule.onNodeWithText("Before start").assertExists()
        submitButton("Start Lottery").performScrollTo().assertIsNotEnabled()
    }

    @Test
    fun validForm_submitsLottery() {
        showEditLotteryScreen(Lottery(), isEdit = false)

        // Pick the campsite (the whole row is clickable)
        composeTestRule.onNodeWithText("Gronk Campground").performClick()

        // Fill in all three periods
        typeDate("Start", 0, "01/01/2027")
        typeDate("End", 0, "01/31/2027")
        typeDate("Start", 1, "02/01/2027")
        typeDate("End", 1, "02/07/2027")
        typeDate("Start", 2, "06/01/2027")
        typeDate("End", 2, "08/31/2027")

        // Now everything is valid, so the button turns on
        submitButton("Start Lottery").performScrollTo().assertIsEnabled().performClick()

        // The lottery sent back should hold what we entered
        assertNotNull(submittedLottery)
        assertEquals("abc123", submittedLottery!!.ownerId)
        assertEquals(listOf("site123"), submittedLottery!!.campsites.map { it.ID })
        assertNotNull(submittedLottery!!.registrationStartDate)
        assertNotNull(submittedLottery!!.bookingEndDate)
        assertNotNull(submittedLottery!!.activeEndDate)
    }

    @Test
    fun existingLottery_showsUpdateAndCurrentValues() {
        val lottery = Lottery(
            id = "lottery1",
            campsites = mutableListOf(campsite),
            numberOfWinners = 3,
            waitlistCapacity = 50
        )
        showEditLotteryScreen(lottery, isEdit = true)

        // Editing uses "Update Lottery"
        composeTestRule.onAllNodesWithText("Update Lottery")[0].assertIsDisplayed()

        // The number boxes start with the lottery's current values
        composeTestRule.onNodeWithText("Num. winners").assertTextContains("3")
        composeTestRule.onNodeWithText("Max. waitlist cap.").assertTextContains("50")
    }
}