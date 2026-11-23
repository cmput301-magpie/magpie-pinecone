package com.example.magpie_pinecone.uiTests.ownerScreensTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
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
                    onQRCode = { qrCodeCampsite = it })
            }
        }
    }

    @Test
    fun showsWelcomeAndTabs() {
        showOwnerHomeScreen(emptyList(), emptyList())

        // Title and both tabs should be on screen
        composeTestRule.onNodeWithText("Welcome Back, Owner!").assertIsDisplayed()
        composeTestRule.onNodeWithText("My Lotteries").assertIsDisplayed()
        composeTestRule.onNodeWithText("My Campsites").assertIsDisplayed()
    }

    @Test
    fun noCampsites_showsEmptyMessage() {
        showOwnerHomeScreen(emptyList(), emptyList())

        // My Campsites is the tab that opens first.
        // The message sits under the list, so we only check that it exists
        composeTestRule.onNodeWithText("You have no campsites yet.").assertExists()
    }

    @Test
    fun campsiteCard_showsDetails() {
        showOwnerHomeScreen(listOf(campsite), emptyList())

        // The card shows the campsite's info
        composeTestRule.onNodeWithText("Gronk Campground").assertIsDisplayed()
        composeTestRule.onNodeWithText("1234 Rizz St").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rating: 5.0").assertIsDisplayed()
        composeTestRule.onNodeWithText("Max Occupancy: 5").assertIsDisplayed()
    }

    @Test
    fun campsiteMenu_callsEditPreviewAndDatesBooked() {
        showOwnerHomeScreen(listOf(campsite), emptyList())

        // Open the "..." menu and pick each option one at a time
        composeTestRule.onNodeWithText("...").performClick()
        composeTestRule.onNodeWithText("Edit").performClick()

        composeTestRule.onNodeWithText("...").performClick()
        composeTestRule.onNodeWithText("Preview").performClick()

        composeTestRule.onNodeWithText("...").performClick()
        composeTestRule.onNodeWithText("Dates Booked").performClick()

        composeTestRule.onNodeWithText("...").performClick()
        composeTestRule.onNodeWithText("Get QR Code").performClick()

        // Each option should send back the campsite it belongs to
        assertEquals("site123", editedCampsite?.ID)
        assertEquals("site123", previewedCampsite?.ID)
        assertEquals("site123", datesBookedCampsite?.ID)
        assertEquals("site123", qrCodeCampsite?.ID)
    }

    @Test
    fun campsiteButtons_callTheirFunctions() {
        showOwnerHomeScreen(emptyList(), emptyList())

        composeTestRule.onNodeWithText("+ Campsite").performClick()
        composeTestRule.onNodeWithText("Campsite Resv.").performClick()

        assertTrue(createCampsitePressed)
        assertTrue(reservationsPressed)
    }

    @Test
    fun noLotteries_showsEmptyMessage() {
        showOwnerHomeScreen(emptyList(), emptyList())

        // Switch to the lotteries tab first
        composeTestRule.onNodeWithText("My Lotteries").performClick()

        composeTestRule.onNodeWithText("You have no lotteries yet.").assertExists()
    }

    @Test
    fun lotteryCard_showsDetails() {
        showOwnerHomeScreen(emptyList(), listOf(lottery))
        composeTestRule.onNodeWithText("My Lotteries").performClick()

        // The lottery is named after its campsite and shows its numbers
        composeTestRule.onNodeWithText("Gronk Campground").assertIsDisplayed()
        composeTestRule.onNodeWithText("Winners: 3").assertIsDisplayed()
        composeTestRule.onNodeWithText("Waitlist capacity: 50").assertIsDisplayed()
    }

    @Test
    fun lotteryMenu_callsEditAndViewWaitlist() {
        showOwnerHomeScreen(emptyList(), listOf(lottery))
        composeTestRule.onNodeWithText("My Lotteries").performClick()

        composeTestRule.onNodeWithText("...").performClick()
        composeTestRule.onNodeWithText("Edit").performClick()

        composeTestRule.onNodeWithText("...").performClick()
        composeTestRule.onNodeWithText("View waitlist").performClick()

        // Both options should send back this lottery
        assertEquals("lottery1", editedLottery?.id)
        assertEquals("lottery1", waitlistLottery?.id)
    }

    @Test
    fun createLotteryButton_callsOnCreateLottery() {
        showOwnerHomeScreen(emptyList(), emptyList())
        composeTestRule.onNodeWithText("My Lotteries").performClick()

        composeTestRule.onNodeWithText("+ Lottery").performClick()
        assertTrue(createLotteryPressed)
    }
}