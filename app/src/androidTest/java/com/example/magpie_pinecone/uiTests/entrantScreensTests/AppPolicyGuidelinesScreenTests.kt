package com.example.magpie_pinecone.uiTests.entrantScreensTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.magpie_pinecone.frontend.entrantScreens.AppPolicyGuidelinesScreen
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AppPolicyGuidelinesScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()   // Shows a screen on the emulator so we can test it

    @Test
    fun showsTitle() {
        composeTestRule.setContent {
            MagpiepineconeTheme { AppPolicyGuidelinesScreen(onBack = { true }) }
        }

        // The title from Figma should be on screen
        composeTestRule.onNodeWithText("App Policy").assertIsDisplayed()
    }

    @Test
    fun backButton_callsOnBack() {
        var backPressed = false
        composeTestRule.setContent {
            MagpiepineconeTheme {
                AppPolicyGuidelinesScreen(onBack = { backPressed = true; true })
            }
        }

        // The shared back button is found by its description
        composeTestRule.onNodeWithContentDescription("Back Button").performClick()
        assertTrue(backPressed)
    }
}