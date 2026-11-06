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
 * @param curUser The current user. Use its details to populate the fields. Modified in the function.
 * @param onUpdateProfile Called after the user's profile has been updated.
 */

@Composable
fun EditProfileScreen(navController: NavController, curUser: User?, onUpdateProfile: (user: User) -> Unit) {

    var name by remember { mutableStateOf("") }     // Stores the text entered into the name box
    var email by remember { mutableStateOf("") }    // Stores the text entered into the email box
    var phone by remember { mutableStateOf("") }    // Stores the text entered into the phone box


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
        if (curUser == null) {
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
                    curUser.name = name
                    curUser.email = email
                    curUser.phoneNumber = phone
                    onUpdateProfile(curUser)
                    navController.popBackStack()
                },
                modifier = Modifier.testTag("saveProfileButton") // for testing
            ) {
                Text("Save Profile")
            }
        }
    }
}