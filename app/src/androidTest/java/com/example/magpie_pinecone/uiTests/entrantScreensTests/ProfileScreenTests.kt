package com.example.magpie_pinecone.uiTests.entrantScreensTests

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.magpie_pinecone.EditProfile
import com.example.magpie_pinecone.Profile
import com.example.magpie_pinecone.backend.UserClasses.AccountType
import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.frontend.entrantScreens.ProfileScreen
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ProfileScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()   // Shows a screen on the emulator so we can test it

    // Same sample values as the backend tests
    private val user = User(
        id = "abc123",
        name = "Baby Gronk",
        email = "Rizz@gmail.com",
        phoneNumber = "780-123-4567",
        accountType = AccountType.Entrant
    )

    /**
     * Shows ProfileScreen inside a small NavHost so its navigation calls work.
     * The Edit Profile route only shows a text, so we can check that we got there.
     */
    private fun showProfileScreen(curUser: User?, onProfileDeleted: () -> Unit = {}) {
        composeTestRule.setContent {
            MagpiepineconeTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Profile) {
                    composable<Profile> {
                        ProfileScreen(navController, curUser, onProfileDeleted)
                    }
                    composable<EditProfile> {
                        Text("Edit Profile Page")
                    }
                }
            }
        }
    }

    @Test
    fun showsUserDetails() {
        showProfileScreen(user)

        // The user's name, email and phone should all be on screen
        composeTestRule.onNodeWithText("Baby Gronk").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rizz@gmail.com").assertIsDisplayed()
        composeTestRule.onNodeWithText("780-123-4567").assertIsDisplayed()
    }

    @Test
    fun nullUser_showsLoading() {
        showProfileScreen(null)

        // While the user is still loading, only the loading text shows
        composeTestRule.onNodeWithText("Loading profile...").assertIsDisplayed()

        // The edit and delete buttons should not exist yet
        composeTestRule.onNodeWithTag("editProfileButton").assertDoesNotExist()
        composeTestRule.onNodeWithTag("deleteProfileButton").assertDoesNotExist()
    }

    @Test
    fun editProfileButton_opensEditProfile() {
        showProfileScreen(user)

        composeTestRule.onNodeWithTag("editProfileButton").performClick()

        // We should now be on the (fake) Edit Profile page
        composeTestRule.onNodeWithText("Edit Profile Page").assertIsDisplayed()
    }

    @Test
    fun deleteProfile_needsTwoConfirms() {
        var deleted = false
        showProfileScreen(user, onProfileDeleted = { deleted = true })

        // First confirm should only open the second warning, not delete yet
        composeTestRule.onNodeWithTag("deleteProfileButton").performClick()
        composeTestRule.onNodeWithTag("firstDeleteConfirmButton").performClick()
        assertFalse(deleted)
        composeTestRule.onNodeWithText("This cannot be undone").assertIsDisplayed()

        // Second confirm actually deletes
        composeTestRule.onNodeWithTag("secondDeleteConfirmButton").performClick()
        assertTrue(deleted)
    }

    @Test
    fun cancelDelete_doesNotDelete() {
        var deleted = false
        showProfileScreen(user, onProfileDeleted = { deleted = true })

        // Open the first warning, then press Cancel
        composeTestRule.onNodeWithTag("deleteProfileButton").performClick()
        composeTestRule.onNodeWithText("Cancel").performClick()

        // Nothing deleted and the warning is gone
        assertFalse(deleted)
        composeTestRule.onNodeWithText("Delete profile?").assertDoesNotExist()
    }
}