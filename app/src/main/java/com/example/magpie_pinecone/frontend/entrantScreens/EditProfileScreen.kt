package com.example.magpie_pinecone.frontend.entrantScreens

import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.backend.UserClasses.UserDB

/**
 * Lets entrants edit their name, email and phone number.
 *
 * @param navController Controls navigation between entrant screens.
 */

@Composable
fun EditProfileScreen(navController: NavController) {

    val userDB = remember { UserDB() }

    // The user's ID is the device ID, the same one PineconeApp uses
    val context = LocalContext.current
    val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    var user by remember { mutableStateOf<User?>(null) }  // Stores the user fetched from the user database

    var name by remember { mutableStateOf("") }     // Stores the text entered into the name box
    var email by remember { mutableStateOf("") }    // Stores the text entered into the email box
    var phone by remember { mutableStateOf("") }    // Stores the text entered into the phone box


    LaunchedEffect(Unit) // Fetch the user when the screen is first displayed
    {
        userDB.readUser(androidId) { fetchedUser ->
            user = fetchedUser
            if (fetchedUser != null) {
                // Fill the text boxes with the user's current details
                name = fetchedUser.name
                email = fetchedUser.email
                phone = fetchedUser.phoneNumber
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        Text(
            text = "Edit Profile",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Only show the boxes once the user has been fetched
        val currentUser = user
        if (currentUser == null) {
            Text("Loading profile...")
        } else {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("nameField") // for testing
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("emailField") // for testing
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("phoneField") // for testing
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Put the entered text into the user, save it, then go back
                    currentUser.name = name
                    currentUser.email = email
                    currentUser.phoneNumber = phone
                    userDB.updateUser(currentUser)
                    navController.popBackStack()
                },
                modifier = Modifier.testTag("saveProfileButton") // for testing
            ) {
                Text("Save Profile")
            }
        }
    }
}