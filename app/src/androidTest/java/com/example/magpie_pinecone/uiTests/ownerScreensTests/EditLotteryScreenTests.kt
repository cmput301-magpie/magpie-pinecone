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
import androidx.compose.ui.test.performTextReplacement
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.frontend.ownerScreens.EditLotteryScreen
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditLotteryScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()   // Shows a screen on the emulator so we can test it

    private val campsite = Campsite(ID = "site123", name = "Gronk Campground")

    // Remember what the screen sent back
    private var submittedLottery: Lottery? = null

    /** Turns "01/31/2027" into a Date, the same way the screen shows dates. */
    private fun date(text: String): Date =
        SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(text)!!

    /**
     * A lottery with everything filled in. A fresh copy is made for every test,
     * because submitting changes the lottery object.
     */
    private fun filledLottery() = Lottery(
        id = "lottery1",
        campsites = mutableListOf(Campsite(ID = "site123", name = "Gronk Campground")),
        numberOfWinners = 3,
        waitlistCapacity = 50,
        registrationStartDate = date("01/01/2027"),
        registrationEndDate = date("01/31/2027"),
        bookingStartDate = date("02/01/2027"),
        bookingEndDate = date("02/07/2027"),
        activeStartDate = date("06/01/2027"),
        activeEndDate = date("08/31/2027")
    )

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
        submitButton("Start Lottery").assertIsNotEnabled()
    }

    @Test
    fun filledForm_submitsLottery() {
        showEditLotteryScreen(filledLottery(), isEdit = false)

        // Everything is filled in, so the button is on
        submitButton("Start Lottery").assertIsEnabled().performClick()

        // The lottery sent back should hold the owner, campsite, numbers and dates
        assertNotNull(submittedLottery)
        assertEquals("abc123", submittedLottery!!.ownerId)
        assertEquals(listOf("site123"), submittedLottery!!.campsites.map { it.ID })
        assertEquals(3, submittedLottery!!.numberOfWinners)
        assertEquals(50, submittedLottery!!.waitlistCapacity)
        assertEquals(date("01/01/2027"), submittedLottery!!.registrationStartDate)
        assertEquals(date("08/31/2027"), submittedLottery!!.activeEndDate)
    }

    @Test
    fun existingLottery_showsCurrentValues() {
        showEditLotteryScreen(filledLottery(), isEdit = true)

        // Editing uses "Update Lottery"
        composeTestRule.onAllNodesWithText("Update Lottery")[0].assertIsDisplayed()

        // The number boxes start with the lottery's current values
        composeTestRule.onNodeWithText("Num. winners").assertTextContains("3")
        composeTestRule.onNodeWithText("Max. waitlist cap.").assertTextContains("50")

        // The registration period shows both of its dates
        composeTestRule.onNodeWithText("Registration period")
            .performScrollTo()
            .assertTextContains("01/01/2027 - 01/31/2027")
    }

    @Test
    fun untickingCampsite_turnsSubmitOff() {
        showEditLotteryScreen(filledLottery(), isEdit = true)

        // The lottery's campsite starts ticked, so the button is on
        submitButton("Update Lottery").assertIsEnabled()

        // Untick it: a lottery needs at least one campsite
        composeTestRule.onNodeWithText("Gronk Campground").performClick()
        submitButton("Update Lottery").assertIsNotEnabled()
    }

    @Test
    fun zeroWinners_showsHintAndTurnsSubmitOff() {
        showEditLotteryScreen(filledLottery(), isEdit = false)

        composeTestRule.onNodeWithText("Num. winners").performTextReplacement("0")

        // A lottery needs at least one winner
        composeTestRule.onNodeWithText("Enter at least 1 winner").assertIsDisplayed()
        submitButton("Start Lottery").assertIsNotEnabled()
    }

    @Test
    fun tappingPeriod_opensCalendar() {
        showEditLotteryScreen(Lottery(), isEdit = false)

        composeTestRule.onNodeWithText("Booking period").performScrollTo().performClick()

        // The calendar pop-up has OK and Cancel buttons
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText("OK").assertIsDisplayed()
    }
}