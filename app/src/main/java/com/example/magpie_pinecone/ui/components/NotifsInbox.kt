package com.example.magpie_pinecone.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.magpie_pinecone.R

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