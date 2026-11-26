package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.magpie_pinecone.EditProfile
import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.ui.components.PillButton
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.example.magpie_pinecone.ui.theme.AvatarLavender
import com.example.magpie_pinecone.ui.theme.DeclineRed
import com.example.magpie_pinecone.ui.theme.PillPurple

/*
 * ProfileScreen.kt contains all relevant functions for displaying the entrant's profile.
 *
 * Composable Functions:
 * ProfileScreen displays the entrant's profile, including profile picture, name, email, phone number, etc. An option to delete profile is provided.
 * ProfileDetail displays a formatted label and value for a detail within ProfileScreen (i.e. name)
 */

/**
 * Displays the entrant's profile information.
 *
 * The screen shows the user's name, email and phone number. The user can
 * open the edit profile screen or permanently delete their profile.
 *
 * @param navController Controls navigation between entrant screens.
 * @param curUser The current user whose information is displayed.
 * @param onProfileDeleted Called after the user confirms profile deletion.
 */
@Composable
fun ProfileScreen(
    navController: NavController,
    curUser: User?,
    onProfileDeleted: () -> Unit = {}
) {
    var showFirstAlert by remember { mutableStateOf(false) }
    var showSecondAlert by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                horizontal = 24.dp,
                vertical = 8.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Shared header provides the centered title and back arrow.
            PineconeTopBar(
                title = "Profile",
                onBack = {
                    navController.popBackStack()
                }
            )

            if (curUser == null) {
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Loading profile...",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Spacer(modifier = Modifier.height(32.dp))

                // Profile avatar from the Figma design.
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            color = AvatarLavender,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Profile",
                        tint = PillPurple,
                        modifier = Modifier.size(82.dp)
                    )
                }

                Spacer(modifier = Modifier.height(44.dp))

                // User details are kept narrow to match the Figma layout.
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.72f)
                        .widthIn(max = 300.dp)
                ) {
                    ProfileDetail(
                        label = "Name",
                        value = curUser.name
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    ProfileDetail(
                        label = "Email",
                        value = curUser.email
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    ProfileDetail(
                        label = "Phone",
                        value = curUser.phoneNumber
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Shared pill button matches the Figma edit action.
                PillButton(
                    text = "Edit Profile",
                    onClick = {
                        navController.navigate(EditProfile)
                    },
                    icon = Icons.Filled.Edit,
                    modifier = Modifier
                        .height(56.dp)
                        .testTag("editProfileButton")
                )
            }
        }

        // Keep the delete action near the bottom of the content area.
        if (curUser != null) {
            TextButton(
                onClick = {
                    showFirstAlert = true
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = DeclineRed
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .testTag("deleteProfileButton")
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null
                )

                Text(
                    text = "Delete Profile",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // First confirmation before allowing permanent deletion.
        if (showFirstAlert) {
            AlertDialog(
                onDismissRequest = {
                    showFirstAlert = false
                },
                title = {
                    Text(
                        text = "Delete profile?",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete your profile?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showFirstAlert = false
                            showSecondAlert = true
                        },
                        modifier = Modifier.testTag(
                            "firstDeleteConfirmButton"
                        )
                    ) {
                        Text(
                            text = "Yes, delete",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showFirstAlert = false
                        }
                    ) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            )
        }

        // Final warning because deleting a profile cannot be undone.
        if (showSecondAlert) {
            AlertDialog(
                onDismissRequest = {
                    showSecondAlert = false
                },
                title = {
                    Text(
                        text = "This cannot be undone",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                text = {
                    Text(
                        text = "Deleting your profile is permanent. " +
                                "All of your profile information will be lost.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSecondAlert = false
                            onProfileDeleted()
                        },
                        modifier = Modifier.testTag(
                            "secondDeleteConfirmButton"
                        )
                    ) {
                        Text(
                            text = "Delete forever",
                            style = MaterialTheme.typography.bodyLarge,
                            color = DeclineRed
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showSecondAlert = false
                        }
                    ) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            )
        }
    }
}

/**
 * Displays one label and value in the profile information section.
 *
 * @param label Description of the profile value.
 * @param value Current value stored for the user.
 */
@Composable
private fun ProfileDetail(
    label: String,
    value: String
) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodyLarge
    )

    Text(
        text = value,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}