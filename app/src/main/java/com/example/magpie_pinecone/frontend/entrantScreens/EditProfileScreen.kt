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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.backend.deleteImageFromFirebase
import com.example.magpie_pinecone.backend.uploadImageToFirebase
import com.example.magpie_pinecone.backend.sharedDataClasses.ImageData
import com.example.magpie_pinecone.frontend.sharedComposables.ProfileImage
import com.example.magpie_pinecone.ui.components.PillButton
import com.example.magpie_pinecone.ui.components.PineconeTextField
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.example.magpie_pinecone.ui.theme.AvatarLavender
import com.example.magpie_pinecone.ui.theme.PillPurple

/*
 * EditProfileScreen.kt contains all relevant functions for displaying the entrant profile edit screen.
 *
 * Composable Functions:
 * EditProfileScreen displays the screen to edit a profile, with editable fields.
 */

/**
 * Lets the current user update their name, email and phone number.
 *
 * @param navController Controls navigation between entrant screens.
 * @param curUser The user whose profile is being edited.
 * @param onUpdateProfile Saves the updated user.
 */
@Composable
fun EditProfileScreen(
    navController: NavController,
    curUser: User?,
    onUpdateProfile: (user: User) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var profileImageData by remember { mutableStateOf<ImageData?>(null) }

    // Fill the fields once the user has been loaded from the database.
    LaunchedEffect(curUser) {
        if (curUser != null) {
            name = curUser.name
            email = curUser.email
            phone = curUser.phoneNumber
            profileImageData = curUser.imageData
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                horizontal = 24.dp,
                vertical = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = AvatarLavender,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                ProfileImage(
                    image = profileImageData,
                    allowEditing = true,
                    onUserUpload = { newImageData ->
                        val oldImageUrl = curUser.imageData?.url
                        profileImageData = newImageData
                        curUser.imageData = newImageData

                        val localUri = newImageData.localUri
                        if (localUri != null) {
                            CoroutineScope(Dispatchers.IO).launch {
                                val uploadResult = uploadImageToFirebase(localUri)

                                uploadResult.onSuccess { downloadUrl ->
                                    newImageData.url = downloadUrl
                                    curUser.imageData = newImageData
                                    profileImageData = newImageData

                                    if (!oldImageUrl.isNullOrEmpty()) {
                                        deleteImageFromFirebase(oldImageUrl)
                                    }
                                }
                            }
                        }
                    }
                )

                if (profileImageData != null && (profileImageData?.localUri != null || !profileImageData?.url.isNullOrEmpty())) {
                    IconButton(
                        onClick = {
                            val imageToDeleteUrl = profileImageData?.url
                            profileImageData = null
                            curUser.imageData = null

                            if (!imageToDeleteUrl.isNullOrEmpty()) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    deleteImageFromFirebase(imageToDeleteUrl)
                                }
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(32.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete profile image",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.78f)
                    .widthIn(max = 320.dp)
            ) {
                PineconeTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Name",
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("nameField")
                )

                Spacer(modifier = Modifier.height(20.dp))

                PineconeTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("emailField")
                )

                Spacer(modifier = Modifier.height(20.dp))

                PineconeTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Phone",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("phoneField")
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            PillButton(
                text = "Save Profile",
                onClick = {
                    curUser.name = name
                    curUser.email = email
                    curUser.phoneNumber = phone
                    curUser.imageData = profileImageData

                    onUpdateProfile(curUser)
                    navController.popBackStack()
                },
                icon = Icons.Filled.Check,
                modifier = Modifier
                    .height(56.dp)
                    .testTag("saveProfileButton")
            )
        }
    }
}