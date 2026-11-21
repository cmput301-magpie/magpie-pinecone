package com.example.magpie_pinecone.frontend.sharedComposables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/*
 * NotifsInbox.kt contains all relevant functions for a notification inbox icon used in the app.
 *
 * Composable Functions:
 * NotifsInbox displays a notification inbox icon, for use in all screens needing it for UI consistency.
 * NotifsInboxPreview displays a preview of NotifsInbox for reference.
 */

@Composable
fun NotifsInbox(onInboxClicked: () -> Unit = {}, modifier: Modifier = Modifier) {
    IconButton (onClick = onInboxClicked, modifier = modifier) {
        Icon(
            imageVector = Icons.Filled.Notifications,
            contentDescription = "Inbox",
        )
    }
}

@Preview
@Composable
fun NotifsInboxPreview() {
    NotifsInbox()
}