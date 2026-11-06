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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.Color
import com.example.magpie_pinecone.AppPolicyGuidelines
import com.example.magpie_pinecone.EditProfile

/**
 * Displays the entrant's name, email and phone number.
 * Entrants can open the edit profile screen from here.
 *
 * @param navController Controls navigation between entrant screens.
 * @param curUser The current user. Use its details to populate the fields.
 * @param onProfileDeleted Called after the entrant's profile has been deleted.
 */

@Composable
fun ProfileScreen(navController: NavController, curUser: User?, onProfileDeleted: () -> Unit = {}) {

    // The user's ID is the device ID, the same one PineconeApp uses
    val context = LocalContext.current

    var showFirstAlert by remember { mutableStateOf(false) }   // Whether the first "delete?" alert is showing
    var showSecondAlert by remember { mutableStateOf(false) }  // Whether the final "cannot be undone" alert is showing

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
        if (curUser == null) {
            Text("Loading profile...")
        } else {

            Text(
                text = "Name",
                fontSize = 20.sp
            )
            Text(curUser.name)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Email",
                fontSize = 20.sp
            )
            Text(curUser.email)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Phone",
                fontSize = 20.sp
            )
            Text(curUser.phoneNumber)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate(EditProfile)
                },
                modifier = Modifier.testTag("editProfileButton") // for testing
            ) {
                Text("Edit Profile")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate(AppPolicyGuidelines)
                },
                modifier = Modifier.testTag("appPolicyButton") // for testing
            ) {
                Text("View App Policies and Guidelines")
            }

            // Red delete link (US 01.08.01)
            TextButton(
                onClick = { showFirstAlert = true },
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Red),
                modifier = Modifier.testTag("deleteProfileButton") // for testing
            ) {
                Text("Delete Profile")
            }

            // First alert: ask if they really want to delete
            if (showFirstAlert) {
                AlertDialog(
                    onDismissRequest = { showFirstAlert = false },
                    title = { Text("Delete profile?") },
                    text = { Text("Are you sure you want to delete your profile?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showFirstAlert = false
                                showSecondAlert = true
                            },
                            modifier = Modifier.testTag("firstDeleteConfirmButton") // for testing
                        ) {
                            Text("Yes, delete")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showFirstAlert = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // Second alert: warn that deleting can not be undone
            if (showSecondAlert) {
                AlertDialog(
                    onDismissRequest = { showSecondAlert = false },
                    title = { Text("This cannot be undone") },
                    text = { Text("Deleting your profile is permanent. All of your profile information will be lost.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showSecondAlert = false
                                onProfileDeleted()
                            },
                            modifier = Modifier.testTag("secondDeleteConfirmButton") // for testing
                        ) {
                            Text("Delete forever")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showSecondAlert = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}
