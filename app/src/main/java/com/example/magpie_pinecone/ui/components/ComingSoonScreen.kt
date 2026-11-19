package com.example.magpie_pinecone.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme

/**
 * Placeholder screen for features that are not built yet (Scan QR, Notifications, Messages...).
 * Leave onBack as null when this screen is a bottom-nav tab (tabs have no back arrow).
 */
@Composable
fun ComingSoonScreen(title: String, onBack: (() -> Unit)? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PineconeTopBar(title = title, onBack = onBack)

        // The two weighted spacers push the message into the middle of the screen
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Outlined.Construction,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Coming soon", style = MaterialTheme.typography.titleLarge)
        Text(text = "This feature isn't ready yet.", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ComingSoonScreenPreview() {
    MagpiepineconeTheme {
        ComingSoonScreen(title = "Scan QR")
    }
}