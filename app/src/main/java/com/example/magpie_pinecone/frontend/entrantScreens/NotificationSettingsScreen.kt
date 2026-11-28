package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme

/*
 * NotificationSettingsScreen.kt contains all relevant functions for displaying
 * the entrant's notification settings.
 * Composable Functions:
 * NotificationSettingsScreen displays the notification settings screen.
 * NotificationSettingsScreenPreview displays a preview for reference.
 */

/**
 * Allows entrants to enable or disable notifications (US 01.10.01).
 *
 * @param receiveNotifications Whether the entrant receives notifications.
 * @param onNotificationChange Called when the notification preference changes.
 * @param onBack Returns to the previous screen.
 */
@Composable
fun NotificationSettingsScreen(
    receiveNotifications: Boolean,
    onNotificationChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PineconeTopBar(
            title = "Notifications",
            onBack = onBack
        )

        Spacer(modifier = Modifier.height(48.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 40.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Receive Notifications",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = receiveNotifications,
                onCheckedChange = onNotificationChange
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun NotificationSettingsScreenPreview() {
    MagpiepineconeTheme {
        var receiveNotifications by remember {
            mutableStateOf(true)
        }

        NotificationSettingsScreen(
            receiveNotifications = receiveNotifications,
            onNotificationChange = {
                receiveNotifications = it
            },
            onBack = {}
        )
    }
}