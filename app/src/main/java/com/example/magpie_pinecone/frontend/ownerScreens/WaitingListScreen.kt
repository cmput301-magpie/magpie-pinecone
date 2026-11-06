package com.example.magpie_pinecone.frontend.ownerScreens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magpie_pinecone.backend.UserClasses.User

/**
 * Shows the entrants who joined the waitlist of a campsite lottery.
 * TO-DO
 * Pass in the selected lottery's real waitlist
 */

@Composable
fun WaitingListScreen(
    entrants: List<User>,
    onBack: () -> Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box() {
            Button(
                modifier = Modifier.align(Alignment.TopStart),
                onClick = { onBack() }
            ) {
                Text("<--")
            }

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = "Waiting List",
                    fontSize = 36.sp
                )

                // Message when nobody has joined the waitlist
                if (entrants.isEmpty()) {
                    Text(
                        modifier = Modifier.padding(20.dp),
                        text = "No entrants on the waitlist",
                        fontSize = 20.sp
                    )
                }

                LazyColumn() {
                    // One row per entrant, showing name and email to identify them
                    items(entrants) { entrant ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(width = 1.dp, color = Color.Black)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = entrant.name, fontSize = 30.sp)
                                Text(text = entrant.email, fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowWaitingListScreenPreview() {
    WaitingListScreen(listOf(User(name = "Sample Entrant", email = "sample@email.com")), { false })
}