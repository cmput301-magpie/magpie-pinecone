package com.example.magpie_pinecone.uiTests.entrantScreensTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.entrantClasses.Entrant
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.frontend.entrantScreens.EntrantHomeScreen
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class EntrantHomeScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()   // Shows a screen on the emulator so we can test it

    // Same sample values as the backend tests
    private val entrant = Entrant(
        id = "abc123",
        name = "Baby Gronk",
        email = "Rizz@gmail.com",
        phoneNumber = "780-123-4567"
    )

    private val campsite = Campsite(
        ID = "site123",
        name = "Gronk Campground",
        location = "1234 Rizz St",
        maxOccupancy = 5,
        price = 38.0,
        rating = 5.0
    )

    // A lottery the entrant has joined but not won
    private val joinedLottery = Lottery(id = "lottery1", campsites = mutableListOf(campsite))

    // A lottery the entrant won (they are in the winners list)
    private val wonLottery = Lottery(
        id = "lottery2",
        campsites = mutableListOf(campsite),
        winners = listOf(entrant)
    )

    // Remember what the buttons sent back
    private var detailsLottery: Lottery? = null
    private var leftLottery: Lottery? = null
    private var reservedLottery: Lottery? = null
    private var myReservationsPressed = false
    private var notificationsPressed = false

    private fun showHomeScreen(lotteries: List<Lottery>, curEntrant: Entrant? = entrant) {
        composeTestRule.setContent {
            MagpiepineconeTheme {
                EntrantHomeScreen(
                    onMyReservations = { myReservationsPressed = true },
                    onNotifications = { notificationsPressed = true },
                    lotteries = lotteries,
                    curEntrant = curEntrant,
                    onReserveLottery = { reservedLottery = it },
                    onLotteryDetails = { detailsLottery = it },
                    onLeaveLottery = { leftLottery = it }
                )
            }
        }
    }

    @Test
    fun nullEntrant_showsNothing() {
        showHomeScreen(emptyList(), curEntrant = null)

        // The screen returns early, so none of the home content exists
        composeTestRule.onNodeWithText("Your Lottery Entries").assertDoesNotExist()
    }

    @Test
    fun showsWelcomeWithName() {
        showHomeScreen(emptyList())

        // The banner says "Welcome back,\nBaby Gronk!" so we look for part of the text
        composeTestRule.onNodeWithText("Baby Gronk", substring = true).assertIsDisplayed()
    }

    @Test
    fun noLotteries_showsEmptyMessage() {
        showHomeScreen(emptyList())

        composeTestRule.onNodeWithText("You have no lottery entries yet.").assertIsDisplayed()
    }

    @Test
    fun lotteryCard_showsCampsiteDetails() {
        showHomeScreen(listOf(joinedLottery))

        // Name, address and price (38.0 is shown as $38) should be on the card
        composeTestRule.onNodeWithText("Gronk Campground").assertIsDisplayed()
        composeTestRule.onNodeWithText("1234 Rizz St").assertIsDisplayed()
        composeTestRule.onNodeWithText("\$38").assertIsDisplayed()
    }

    @Test
    fun joinedLottery_detailsAndLeaveWork() {
        showHomeScreen(listOf(joinedLottery))

        // Not won yet, so there should be no Reserve button
        composeTestRule.onNodeWithText("Reserve").assertDoesNotExist()

        composeTestRule.onNodeWithText("Details").performClick()
        composeTestRule.onNodeWithText("Leave").performClick()

        // Both buttons should send back the lottery they belong to
        assertEquals("lottery1", detailsLottery?.id)
        assertEquals("lottery1", leftLottery?.id)
    }

    @Test
    fun wonLottery_showsReserve() {
        showHomeScreen(listOf(wonLottery))

        // Winners see the win message and Reserve instead of Details/Leave
        composeTestRule.onNodeWithText("You won the lottery!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Details").assertDoesNotExist()

        composeTestRule.onNodeWithText("Reserve").performClick()
        assertEquals("lottery2", reservedLottery?.id)
    }

    @Test
    fun myReservations_callsCallback() {
        showHomeScreen(emptyList())

        composeTestRule.onNodeWithText("My reservations").performClick()
        assertTrue(myReservationsPressed)
    }

    @Test
    fun inbox_callsOnNotifications() {
        showHomeScreen(emptyList())

        // The shared inbox icon is found by its description
        composeTestRule.onNodeWithContentDescription("Inbox").performClick()
        assertTrue(notificationsPressed)
    }
}