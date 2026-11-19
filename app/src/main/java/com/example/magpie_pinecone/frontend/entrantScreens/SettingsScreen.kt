package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.example.magpie_pinecone.ui.theme.AvatarLavender
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import com.example.magpie_pinecone.ui.theme.PillPurple

/**
 * Settings tab for entrants (US 01.11.01).
 * Each row just calls one of the functions passed in, so EntrantApp decides where it goes.
 *
 * @param onComingSoon Used by Notifications and Messages, which are not built yet.
 */
@Composable
fun SettingsScreen(
    onProfile: () -> Unit,
    onViewReservations: () -> Unit,
    onAppPolicy: () -> Unit,
    onComingSoon: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PineconeTopBar(title = "Settings")

        Spacer(modifier = Modifier.height(16.dp))

        // Lavender avatar circle with a person icon
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(AvatarLavender),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint = PillPurple,
                modifier = Modifier.size(56.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // One rounded card holding all the menu rows
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            SettingsRow(icon = Icons.Outlined.Person, text = "Profile", onClick = onProfile)
            SettingsRow(icon = Icons.Outlined.Schedule, text = "View Reservations", onClick = onViewReservations)
            SettingsRow(icon = Icons.Outlined.Notifications, text = "Notifications", onClick = onComingSoon)
            SettingsRow(icon = Icons.Outlined.Policy, text = "App Policy and Guidelines", onClick = onAppPolicy)
            SettingsRow(icon = Icons.Outlined.Email, text = "Messages", onClick = onComingSoon)
        }
    }
}

/**
 * One row in the settings card: icon, text, and a ">" arrow on the right.
 */
@Composable
private fun SettingsRow(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        // weight(1f) makes the text take up the empty space, pushing the arrow to the right
        Text(text = text, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    MagpiepineconeTheme {
        SettingsScreen(onProfile = {}, onViewReservations = {}, onAppPolicy = {}, onComingSoon = {})
    }
}