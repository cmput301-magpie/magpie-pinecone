package com.example.magpie_pinecone.uiTests.ownerScreensTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.frontend.ownerScreens.OwnerHomeScreen
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class OwnerHomeScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()   // Shows a screen on the emulator so we can test it

    // Same sample campsite as the entrant tests
    private val campsite = Campsite(
        ID = "site123",
        name = "Gronk Campground",
        location = "1234 Rizz St",
        maxOccupancy = 5,
        price = 38.0,
        rating = 5.0
    )

    private val lottery = Lottery(
        id = "lottery1",
        ownerId = "abc123",
        campsites = mutableListOf(campsite),
        numberOfWinners = 3,
        waitlistCapacity = 50
    )

    // Remember what the buttons and menus sent back
    private var editedLottery: Lottery? = null
    private var waitlistLottery: Lottery? = null
    private var createLotteryPressed = false
    private var editedCampsite: Campsite? = null
    private var previewedCampsite: Campsite? = null
    private var datesBookedCampsite: Campsite? = null
    private var qrCodeCampsite: Campsite? = null
    private var createCampsitePressed = false
    private var reservationsPressed = false

    private fun showOwnerHomeScreen(campsites: List<Campsite>, lotteries: List<Lottery>) {
        composeTestRule.setContent {
            MagpiepineconeTheme {
                OwnerHomeScreen(
                    campsites = campsites,
                    lotteries = lotteries,
                    onEditLottery = { editedLottery = it },
                    onCreateLottery = { createLotteryPressed = true },
                    onViewWaitlist = { waitlistLottery = it },
                    onEditCampsite = { editedCampsite = it },
                    onCreateCampsite = { createCampsitePressed = true },
                    onPreviewCampsite = { previewedCampsite = it },
                    onDatesBooked = { datesBookedCampsite = it },
                    onViewReservations = { reservationsPressed = true },
                    onQRCode = { qrCodeCampsite = it }
                )
            }
        }
    }

    /** Opens the "..." menu on the card and picks one option. */
    private fun pickFromMenu(option: String) {
        composeTestRule.onNodeWithContentDescription("More options").performClick()
        composeTestRule.onNodeWithText(option).performClick()
    }

    @Test
    fun showsWelcomeAndTabs() {
        showOwnerHomeScreen(emptyList(), emptyList())

        // The banner says "Welcome back,\nOwner!" so we look for part of the text
        composeTestRule.onNodeWithText("Owner!", substring = true).assertIsDisplayed()

        // Both parts of the tab switch should be on screen
        composeTestRule.onNodeWithText("My Lotteries").assertIsDisplayed()
        composeTestRule.onNodeWithText("My Campsites").assertIsDisplayed()
    }

    @Test
    fun noCampsites_showsEmptyMessage() {
        showOwnerHomeScreen(emptyList(), emptyList())

        // My Campsites is the tab that opens first
        composeTestRule.onNodeWithText("You have no campsites yet.").assertIsDisplayed()
    }

    @Test
    fun campsiteCard_showsDetails() {
        showOwnerHomeScreen(listOf(campsite), emptyList())

        // Name, address, price (38.0 is shown as $38), rating and occupancy
        composeTestRule.onNodeWithText("Gronk Campground").assertIsDisplayed()
        composeTestRule.onNodeWithText("1234 Rizz St").assertIsDisplayed()
        composeTestRule.onNodeWithText("\$38").assertIsDisplayed()
        composeTestRule.onNodeWithText("5.0").assertIsDisplayed()
        composeTestRule.onNodeWithText("5").assertIsDisplayed()
    }

    @Test
    fun campsiteMenu_callsEveryOption() {
        showOwnerHomeScreen(listOf(campsite), emptyList())

        // Open the "..." menu and pick each option one at a time
        pickFromMenu("Edit")
        pickFromMenu("Preview")
        pickFromMenu("Dates Booked")
        pickFromMenu("Get QR Code")

        // Each option should send back the campsite it belongs to
        assertEquals("site123", editedCampsite?.ID)
        assertEquals("site123", previewedCampsite?.ID)
        assertEquals("site123", datesBookedCampsite?.ID)
        assertEquals("site123", qrCodeCampsite?.ID)
    }

    @Test
    fun campsiteButtons_callTheirFunctions() {
        showOwnerHomeScreen(emptyList(), emptyList())

        composeTestRule.onNodeWithText("Campsite").performClick()
        composeTestRule.onNodeWithText("Campsite Resv.").performClick()

        assertTrue(createCampsitePressed)
        assertTrue(reservationsPressed)
    }

    @Test
    fun noLotteries_showsEmptyMessage() {
        showOwnerHomeScreen(emptyList(), emptyList())

        // Switch to the lotteries tab first
        composeTestRule.onNodeWithText("My Lotteries").performClick()

        composeTestRule.onNodeWithText("You have no lotteries yet.").assertIsDisplayed()
    }

    @Test
    fun lotteryCard_showsDetails() {
        showOwnerHomeScreen(emptyList(), listOf(lottery))
        composeTestRule.onNodeWithText("My Lotteries").performClick()

        // The lottery is named after its campsite and shows its numbers
        composeTestRule.onNodeWithText("Gronk Campground").assertIsDisplayed()
        composeTestRule.onNodeWithText("Winners: 3").assertIsDisplayed()
        composeTestRule.onNodeWithText("Waitlist capacity: 50").assertIsDisplayed()

        // No registration end date was set, so it says TBD
        composeTestRule.onNodeWithText("Registration ends TBD").assertIsDisplayed()
    }

    @Test
    fun lotteryMenu_callsEditAndViewWaitlist() {
        showOwnerHomeScreen(emptyList(), listOf(lottery))
        composeTestRule.onNodeWithText("My Lotteries").performClick()

        pickFromMenu("Edit")
        pickFromMenu("View waitlist")

        // Both options should send back this lottery
        assertEquals("lottery1", editedLottery?.id)
        assertEquals("lottery1", waitlistLottery?.id)
    }

    @Test
    fun createLotteryButton_callsOnCreateLottery() {
        showOwnerHomeScreen(emptyList(), emptyList())
        composeTestRule.onNodeWithText("My Lotteries").performClick()

        composeTestRule.onNodeWithText("Lottery").performClick()
        assertTrue(createLotteryPressed)
    }

    @Test
    fun lotteryCard_withoutCapacity_showsNoLimit() {
        showOwnerHomeScreen(emptyList(), listOf(lottery.copy(waitlistCapacity = null)))
        composeTestRule.onNodeWithText("My Lotteries").performClick()

        composeTestRule.onNodeWithText("Waitlist capacity: No limit").assertIsDisplayed()
    }
}