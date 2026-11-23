package com.example.magpie_pinecone.frontend.adminScreens

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Preview
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme

@Composable
fun AdminHomeScreen(
    onCampsiteBrowse: () -> Unit,
    onProfileBrowse: () -> Unit,
    onImageBrowse: () -> Unit,
    onNotificationLog: () -> Unit,
    onMessageLog: () -> Unit,
    onEntrantView: () -> Unit,
    onOwnerView: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PineconeTopBar(title = "Admin Dashboard")

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            AdminHomeRow(icon = Icons.Outlined.LocationOn, text = "Browse Campsites", onClick = onCampsiteBrowse)
            AdminHomeRow(icon = Icons.Outlined.Person, text = "Browse Profiles", onClick = onProfileBrowse)
            AdminHomeRow(icon = Icons.Outlined.Image, text = "Browse Images", onClick = onImageBrowse)
            AdminHomeRow(icon = Icons.Outlined.Notifications, text = "Notification Log", onClick = onNotificationLog)
            AdminHomeRow(icon = Icons.Outlined.ChatBubbleOutline, text = "Message Log", onClick = onMessageLog)
            AdminHomeRow(icon = Icons.Outlined.Preview, text = "View as Entrant", onClick = onEntrantView)
            AdminHomeRow(icon = Icons.Outlined.Preview, text = "View as Owner", onClick = onOwnerView)
        }
    }
}

/**
 * One row in the admin home screen: icon, text, and a ">" arrow on the right.
 */
@Composable
private fun AdminHomeRow(icon: ImageVector, text: String, onClick: () -> Unit) {
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

@Preview
@Composable
fun AdminHomeScreenPreview() {
    MagpiepineconeTheme {
        AdminHomeScreen(
            {},
            {},
            {},
            {},
            {},
            {},
            {}
        )
    }
}