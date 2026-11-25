package com.example.magpie_pinecone.frontend.ownerScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme

/*
 * WaitingListScreen.kt contains all relevant functions for displaying entrants who joined the waitlist of a campsite lottery.
 *
 * Composable Functions:
 * WaitingListScreen displays all entrants who joined the waitlist for a campsite lottery.
 * EntrantRow displays one entrant's name and email.
 * ShowWaitingListScreenPreview displays a preview of WaitingListScreen for reference.
 */

/**
 * Displays the entrants on a lottery's waiting list, in one rounded card.
 *
 * @param entrants The entrants who joined the lottery's waitlist.
 * @param onBack Returns to the previous screen.
 */
@Composable
fun WaitingListScreen(
    entrants: List<User>,
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
            title = "Waiting List",
            onBack = onBack
        )

        if (entrants.isEmpty()) {
            // Grey message in the middle, like the other empty screens
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No entrants on the waitlist",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // How many entrants joined, e.g. "1 entrant" or "3 entrants"
            val entrantWord = if (entrants.size == 1) "entrant" else "entrants"
            Text(
                text = "${entrants.size} $entrantWord on the waitlist",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(
                    top = 4.dp,
                    bottom = 16.dp
                )
            )

            // One rounded card holding every entrant, with a line between each
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                LazyColumn {
                    itemsIndexed(entrants) { index, entrant ->
                        // A thin line between rows, but not above the first one
                        if (index > 0) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }

                        EntrantRow(
                            entrant = entrant
                        )
                    }
                }
            }
        }
    }
}

/**
 * One entrant on the waiting list: name, with their email in grey under it.
 *
 * @param entrant The entrant shown in this row.
 */
@Composable
private fun EntrantRow(
    entrant: User
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
    ) {
        Text(
            text = entrant.name.ifBlank { "Unnamed entrant" },
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = entrant.email,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ShowWaitingListScreenPreview() {
    MagpiepineconeTheme {
        WaitingListScreen(
            entrants = listOf(
                User(id = "1", name = "Sample Entrant", email = "entrant@example.com"),
                User(id = "2", name = "Another Entrant", email = "another@example.com")
            ),
            onBack = { }
        )
    }
}