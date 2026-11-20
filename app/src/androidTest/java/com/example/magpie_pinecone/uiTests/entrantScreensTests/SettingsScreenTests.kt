package com.example.magpie_pinecone.uiTests.entrantScreensTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.magpie_pinecone.frontend.entrantScreens.SettingsScreen
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SettingsScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()   // Shows a screen on the emulator so we can test it

    // Remember which rows were pressed
    private var profilePressed = false
    private var reservationsPressed = false
    private var policyPressed = false
    private var comingSoonCount = 0

    private fun showSettingsScreen() {
        composeTestRule.setContent {
            MagpiepineconeTheme {
                SettingsScreen(
                    onProfile = { profilePressed = true },
                    onViewReservations = { reservationsPressed = true },
                    onAppPolicy = { policyPressed = true },
                    onComingSoon = { comingSoonCount++ }
                )
            }
        }
    }

    @Test
    fun showsAllRows() {
        showSettingsScreen()

        // Every menu row from Figma should be on screen
        composeTestRule.onNodeWithText("Settings").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
        composeTestRule.onNodeWithText("View Reservations").assertIsDisplayed()
        composeTestRule.onNodeWithText("Notifications").assertIsDisplayed()
        composeTestRule.onNodeWithText("App Policy and Guidelines").assertIsDisplayed()
        composeTestRule.onNodeWithText("Messages").assertIsDisplayed()
    }

    @Test
    fun profileRow_callsOnProfile() {
        showSettingsScreen()

        composeTestRule.onNodeWithText("Profile").performClick()
        assertTrue(profilePressed)
    }

    @Test
    fun reservationsAndPolicyRows_callTheirFunctions() {
        showSettingsScreen()

        composeTestRule.onNodeWithText("View Reservations").performClick()
        composeTestRule.onNodeWithText("App Policy and Guidelines").performClick()

        // Each row should call its own function
        assertTrue(reservationsPressed)
        assertTrue(policyPressed)
    }

    @Test
    fun notificationsAndMessages_openComingSoon() {
        showSettingsScreen()

        composeTestRule.onNodeWithText("Notifications").performClick()
        composeTestRule.onNodeWithText("Messages").performClick()

        // Both rows are not built yet, so both go to Coming Soon
        assertEquals(2, comingSoonCount)
    }
}