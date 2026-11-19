package com.example.magpie_pinecone.frontend.sharedComposables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.magpie_pinecone.R

@Composable
fun NotifsInbox(onInboxClicked: () -> Unit = {}, modifier: Modifier = Modifier) {
    IconButton (onClick = onInboxClicked, modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_mail_outline_24),
            contentDescription = "Inbox",
        )
    }
}

@Preview
@Composable
fun NotifsInboxPreview() {
    NotifsInbox()
}