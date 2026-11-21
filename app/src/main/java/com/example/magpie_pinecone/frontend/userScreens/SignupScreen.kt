package com.example.magpie_pinecone.frontend.userScreens

import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.backend.UserClasses.AccountType
import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.backend.UserClasses.UserDB
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import androidx.compose.foundation.layout.PaddingValues

/*
 * SignupScreen.kt contains all relevant functions for displaying the new user sign-up screen.
 *
 * Composable Functions:
 * SignupScreen displays the new user sign-up screen, with field inputs and toggle to be an entrant or owner.
 * ProfileTextField displays a text field in the SignupScreen.
 * SignupButton displays a button to sign the entrant/owner up.
 * SignupScreenPreview displays a preview of SignupScreen for reference.
 */

/**
 * Displays the profile setup screen for a new user.
 *
 * The user enters their contact information and chooses whether the
 * account should be an entrant or campsite owner. The device ID is used
 * as the user identifier, matching the existing account setup flow.
 *
 * @param modifier Modifier applied to the profile setup screen.
 * @param onClickSignUp Called after the new user is saved.
 * @param onBack Called when the user taps the back arrow.
 */
@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    onClickSignUp: () -> Unit,
    onBack: () -> Unit = {}
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isOwner by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val androidId = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )

    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val formWidth = minOf(maxWidth * 0.68f, 300.dp)

        val buttonWidth = minOf(
            maxOf(maxWidth * 0.32f, 128.dp),
            140.dp
        )

        val topSpacing = minOf(maxHeight * 0.065f, 56.dp)
        val formSpacing = minOf(maxHeight * 0.06f, 56.dp)
        val fieldSpacing = minOf(maxHeight * 0.03f, 28.dp)
        val switchSpacing = minOf(maxHeight * 0.03f, 24.dp)
        val buttonBottomSpacing = minOf(maxHeight * 0.08f, 72.dp)

        val compactScreen = maxHeight < 700.dp

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .statusBarsPadding()
                    .padding(
                        top = topSpacing,
                        bottom = if (compactScreen) 24.dp else 160.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Return to the welcome screen.
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 24.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Profile information fields.
                Column(
                    modifier = Modifier.width(formWidth)
                ) {
                    Text(
                        text = "Set up your profile",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(formSpacing))

                    ProfileTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = "First name"
                    )

                    Spacer(modifier = Modifier.height(fieldSpacing))

                    ProfileTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = "Last name"
                    )

                    Spacer(modifier = Modifier.height(fieldSpacing))

                    ProfileTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email"
                    )

                    Spacer(modifier = Modifier.height(fieldSpacing))

                    ProfileTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Phone"
                    )
                }

                Spacer(modifier = Modifier.height(switchSpacing))

                // Keep the Entrant/Owner choice that is part of the current app flow.
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Entrant",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Switch(
                        checked = isOwner,
                        onCheckedChange = { isOwner = it }
                    )

                    Text(
                        text = "Owner",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // On shorter screens, keep the button in the scrollable content
                // so it cannot overlap the profile fields or account switch.
                if (compactScreen) {
                    Spacer(modifier = Modifier.height(32.dp))

                    SignupButton(
                        width = buttonWidth,
                        onClick = {
                            saveUser(
                                androidId = androidId,
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                phone = phone,
                                isOwner = isOwner,
                                onClickSignUp = onClickSignUp
                            )
                        }
                    )
                }
            }

            // On normal-height screens, keep the button near its Figma position.
            if (!compactScreen) {
                SignupButton(
                    width = buttonWidth,
                    onClick = {
                        saveUser(
                            androidId = androidId,
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            phone = phone,
                            isOwner = isOwner,
                            onClickSignUp = onClickSignUp
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(bottom = buttonBottomSpacing)
                )
            }
        }
    }
}

/**
 * Underline-style field used on the profile setup screen.
 *
 * The shared PineconeTextField uses an outlined box, while this screen's
 * Figma uses a label, value, clear button, and single underline.
 *
 * @param value Current text stored in the field.
 * @param onValueChange Called whenever the field value changes.
 * @param label Label displayed above the entered value.
 */
@Composable
private fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.weight(1f)
            )

            if (value.isNotEmpty()) {
                IconButton(
                    onClick = { onValueChange("") }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Cancel,
                        contentDescription = "Clear $label"
                    )
                }
            }
        }

        HorizontalDivider()
    }
}

/**
 * Primary action button used on the profile setup screen.
 *
 * @param width Responsive width calculated by SignupScreen.
 * @param onClick Called when the user completes profile setup.
 * @param modifier Modifier used to position the button.
 */
@Composable
private fun SignupButton(
    width: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(width)
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(
            horizontal = 12.dp,
            vertical = 0.dp
        )
    ) {
        Text(
            text = "Get Started",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1        )
    }
}

/**
 * Saves the profile using the existing User and UserDB classes.
 *
 * The account type is selected using the Entrant/Owner switch.
 */
private fun saveUser(
    androidId: String,
    firstName: String,
    lastName: String,
    email: String,
    phone: String,
    isOwner: Boolean,
    onClickSignUp: () -> Unit
) {
    val accountType = if (isOwner) {
        AccountType.Owner
    } else {
        AccountType.Entrant
    }

    // Existing profile screens store the user's full name in one field.
    val user = User(
        androidId,
        "$firstName $lastName",
        email,
        phone,
        accountType
    )

    UserDB().addUser(user)
    onClickSignUp()
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun SignupScreenPreview() {
    MagpiepineconeTheme {
        SignupScreen(
            onClickSignUp = {},
            onBack = {}
        )
    }
}