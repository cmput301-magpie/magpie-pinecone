package com.example.magpie_pinecone.frontend.sharedComposables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

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