package com.example.magpie_pinecone.uiTests.entrantScreensTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.magpie_pinecone.frontend.entrantScreens.CampsiteResultScreen
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CampsiteResultScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()   // Shows a screen on the emulator so we can test it

    @Test
    fun showsTitleAndMessage() {
        composeTestRule.setContent {
            MagpiepineconeTheme {
                CampsiteResultScreen(
                    onBack = { true },
                    title = "Campsite Reserved",
                    message = "Your reservation was placed successfully!"
                )
            }
        }

        // The title and message passed in should be shown
        composeTestRule.onNodeWithText("Campsite Reserved").assertIsDisplayed()
        composeTestRule.onNodeWithText("Your reservation was placed successfully!").assertIsDisplayed()
    }

    @Test
    fun showsDownloadCsvButton() {
        composeTestRule.setContent {
            MagpiepineconeTheme { CampsiteResultScreen(onBack = { true }) }
        }

        composeTestRule.onNodeWithText("Download CSV").assertIsDisplayed()
    }

    @Test
    fun backButton_callsOnBack() {
        var backPressed = false
        composeTestRule.setContent {
            MagpiepineconeTheme {
                CampsiteResultScreen(onBack = { backPressed = true; true })
            }
        }

        // This screen still has the old "<--" text button
        composeTestRule.onNodeWithText("<--").performClick()
        assertTrue(backPressed)
    }
}