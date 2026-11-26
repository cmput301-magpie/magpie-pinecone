package com.example.magpie_pinecone.uiTests.ownerScreensTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.backend.entrantClasses.Entrant
import com.example.magpie_pinecone.frontend.ownerScreens.WaitingListScreen
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class WaitingListScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()   // Shows a screen on the emulator so we can test it

    // Same sample entrant as the backend tests, plus a second one
    private val babyGronk = Entrant(
        id = "abc123",
        name = "Baby Gronk",
        email = "Rizz@gmail.com",
        phoneNumber = "780-123-4567"
    )
    private val kaiCenat = Entrant(id = "def456", name = "Kai Cenat", email = "Kai@gmail.com")

    private var backPressed = false

    private fun showWaitingListScreen(entrants: List<User>) {
        composeTestRule.setContent {
            MagpiepineconeTheme {
                WaitingListScreen(entrants = entrants, onBack = { backPressed = true })
            }
        }
    }

    @Test
    fun showsEveryEntrant() {
        showWaitingListScreen(listOf(babyGronk, kaiCenat))

        // Each entrant's name and email should be listed
        composeTestRule.onNodeWithText("Baby Gronk").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rizz@gmail.com").assertIsDisplayed()
        composeTestRule.onNodeWithText("Kai Cenat").assertIsDisplayed()
        composeTestRule.onNodeWithText("Kai@gmail.com").assertIsDisplayed()
    }

    @Test
    fun emptyWaitlist_showsMessage() {
        showWaitingListScreen(emptyList())

        composeTestRule.onNodeWithText("No entrants on the waitlist").assertIsDisplayed()
    }

    @Test
    fun showsHowManyEntrants() {
        showWaitingListScreen(listOf(babyGronk, kaiCenat))

        // Two entrants joined, so the subtitle counts them
        composeTestRule.onNodeWithText("2 entrants on the waitlist").assertIsDisplayed()
    }

    @Test
    fun backButton_callsOnBack() {
        showWaitingListScreen(emptyList())

        composeTestRule.onNodeWithContentDescription("Back Button").performClick()
        assertTrue(backPressed)
    }
}