package com.example.magpie_pinecone.uiTests.entrantScreensTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.magpie_pinecone.EditProfile
import com.example.magpie_pinecone.backend.UserClasses.AccountType
import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.frontend.entrantScreens.EditProfileScreen
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import com.example.magpie_pinecone.Profile

class EditProfileScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()   // Shows a screen on the emulator so we can test it

    // Make a fresh user for every test, because saving changes the user object
    private fun newUser() = User(
        id = "abc123",
        name = "Baby Gronk",
        email = "Rizz@gmail.com",
        phoneNumber = "780-123-4567",
        accountType = AccountType.Entrant
    )
    private fun showEditProfileScreen(curUser: User?, onUpdateProfile: (User) -> Unit = {}) {
        composeTestRule.setContent {
            MagpiepineconeTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Profile) {
                    composable<Profile> {
                        Text("Previous Page")
                    }
                    composable<EditProfile> {
                        EditProfileScreen(navController, curUser, onUpdateProfile)
                    }
                }
                // Open Edit Profile on top of the previous page, like the real app does
                LaunchedEffect(Unit) {
                    navController.navigate(EditProfile)
                }
            }
        }
    }

    @Test
    fun fieldsStartWithCurrentDetails() {
        showEditProfileScreen(newUser())

        // Each box should already hold the user's current value
        composeTestRule.onNodeWithTag("nameField").assertTextContains("Baby Gronk")
        composeTestRule.onNodeWithTag("emailField").assertTextContains("Rizz@gmail.com")
        composeTestRule.onNodeWithTag("phoneField").assertTextContains("780-123-4567")
    }

    @Test
    fun saveProfile_updatesUser() {
        var savedUser: User? = null
        showEditProfileScreen(newUser(), onUpdateProfile = { savedUser = it })

        // Replace each field with new text

        composeTestRule.onNodeWithTag("nameField").performTextReplacement("Kai Cenat")
        composeTestRule.onNodeWithTag("emailField").performTextReplacement("Kai@gmail.com")
        composeTestRule.onNodeWithTag("phoneField").performTextReplacement("587-765-4321")

        composeTestRule.onNodeWithTag("saveProfileButton").performClick()

        // onUpdateProfile should get the user with the new values (same id)
        assertNotNull(savedUser)
        assertEquals("abc123", savedUser!!.id)
        assertEquals("Kai Cenat", savedUser!!.name)
        assertEquals("Kai@gmail.com", savedUser!!.email)
        assertEquals("587-765-4321", savedUser!!.phoneNumber)
        composeTestRule.onNodeWithText("Previous Page").assertIsDisplayed()
    }

    @Test
    fun nullUser_showsLoading() {
        showEditProfileScreen(null)

        // No fields or save button until the user is loaded
        composeTestRule.onNodeWithText("Loading profile...").assertIsDisplayed()
        composeTestRule.onNodeWithTag("nameField").assertDoesNotExist()
        composeTestRule.onNodeWithTag("saveProfileButton").assertDoesNotExist()
    }
}