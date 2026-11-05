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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * TO-DO
 * Link actual entrant data to loop over
 */

@Composable
fun WaitingListScreen(
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

                LazyColumn() {
                    // Single placeholder entrant, change to iterate over list later
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(width = 1.dp, color = Color.Black)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Entrant",
                                fontSize = 30.sp
                            )
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
    WaitingListScreen({ false })
}
