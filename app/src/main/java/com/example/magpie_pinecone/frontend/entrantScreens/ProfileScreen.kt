package com.example.magpie_pinecone.frontend.entrantScreens

import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
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
 * Displays the entrant's name, email and phone number.
 * Entrants can open the edit profile screen from here.
 *
 * @param navController Controls navigation between entrant screens.
 */

@Composable
fun ProfileScreen(navController: NavController) {

    val userDB = remember { UserDB() }

    // The user's ID is the device ID, the same one PineconeApp uses
    val context = LocalContext.current
    val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    var user by remember { mutableStateOf<User?>(null) }  // Stores the user fetched from the user database


    LaunchedEffect(Unit) // Fetch the user when the screen is first displayed
    {
        userDB.readUser(androidId) { fetchedUser ->
            user = fetchedUser
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        Text(
            text = "Profile",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Only show the details once the user has been fetched
        val currentUser = user
        if (currentUser == null) {
            Text("Loading profile...")
        } else {

            Text(
                text = "Name",
                fontSize = 20.sp
            )
            Text(currentUser.name)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Email",
                fontSize = 20.sp
            )
            Text(currentUser.email)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Phone",
                fontSize = 20.sp
            )
            Text(currentUser.phoneNumber)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("editProfile")
                },
                modifier = Modifier.testTag("editProfileButton") // for testing
            ) {
                Text("Edit Profile")
            }
        }
    }
}
