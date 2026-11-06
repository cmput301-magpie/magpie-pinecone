 package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magpie_pinecone.EditLottery
import com.example.magpie_pinecone.R
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery

 /**
 * Display's Entrant's Home Screen
 * TO-DO
 * Link to entrant's actual lotteries to loop over
 * Implement check for if in lottery or not to switch between buttons
 * Implement links to Notifs (after halfway point), Details, Leave, Reserve, My Reservations buttons
 */

@Composable
fun EntrantHomeScreen(
     lotteries: List<Lottery>,
     onMyReservations: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title, may switch entrant out with user's actual name
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = "Welcome Back, Entrant!",
                    fontSize = 36.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Lottery Entries",
                        fontSize = 20.sp
                    )

                    Button(
                        onClick = {}
                    ) {
                        Text("Notifs (0)")
                    }
                }

                LazyColumn(modifier = Modifier.padding(vertical = 10.dp)) {
                    // For now, just a single card as placeholder, will need to change into loop over all campsites
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().height(240.dp).padding(10.dp)
                        ) {
                            Box() {
                                // Placeholder for whether in lottery or not
                                val inLottery = true

                                Column(
                                    modifier = Modifier.fillMaxSize().padding(20.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        // Campsite Details
                                        Column() {
                                            Text(
                                                text = "Campsite",
                                                fontSize = 32.sp
                                            )

                                            Text(
                                                text = "Rating: 5.0",
                                                fontSize = 16.sp
                                            )

                                            Text(
                                                text = "Max Capacity: 5",
                                                fontSize = 16.sp
                                            )

                                            Text(
                                                text = "$38",
                                                fontSize = 16.sp
                                            )

                                            Text(
                                                text = "1234 567 St",
                                                fontSize = 16.sp
                                            )
                                        }

                                        // Placeholder Icon Image
                                        Image(
                                            painter = painterResource(id = R.drawable.placeholdercampsiteimage),
                                            contentDescription = "Campsite Image"
                                        )
                                    }

                                    if (inLottery) {
                                        // Not In Lottery Buttons

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "You won the lottery!"
                                            )

                                            Button(
                                                onClick = {},
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.Green
                                                )
                                            ) {
                                                Text("Reserve")
                                            }
                                        }
                                    } else {
                                        // In Lottery Buttons

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceEvenly
                                        ) {
                                            Button(
                                                onClick = {}
                                            ) {
                                                Text("Details")
                                            }

                                            Button(
                                                onClick = {},
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.Red
                                                )
                                            ) {
                                                Text("Leave")
                                            }
                                        }
                                    }
                                }


                            }
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth().height(240.dp).padding(10.dp)
                        ) {
                            Box() {
                                // Placeholder for whether in lottery or not
                                val inLottery = false

                                Column(
                                    modifier = Modifier.fillMaxSize().padding(20.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        // Campsite Details
                                        Column() {
                                            Text(
                                                text = "Campsite",
                                                fontSize = 32.sp
                                            )

                                            Text(
                                                text = "Rating: 5.0",
                                                fontSize = 16.sp
                                            )

                                            Text(
                                                text = "Max Capacity: 5",
                                                fontSize = 16.sp
                                            )

                                            Text(
                                                text = "$38",
                                                fontSize = 16.sp
                                            )

                                            Text(
                                                text = "1234 567 St",
                                                fontSize = 16.sp
                                            )
                                        }

                                        // Placeholder Icon Image
                                        Image(
                                            painter = painterResource(id = R.drawable.placeholdercampsiteimage),
                                            contentDescription = "Campsite Image"
                                        )
                                    }

                                    if (inLottery) {
                                        // Not In Lottery Buttons

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "You won the lottery!"
                                            )

                                            Button(
                                                onClick = {},
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.Green
                                                )
                                            ) {
                                                Text("Reserve")
                                            }
                                        }
                                    } else {
                                        // In Lottery Buttons

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceEvenly
                                        ) {
                                            Button(
                                                onClick = {}
                                            ) {
                                                Text("Details")
                                            }

                                            Button(
                                                onClick = {},
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.Red
                                                )
                                            ) {
                                                Text("Leave")
                                            }
                                        }
                                    }
                                }


                            }
                        }

                    }
                }

            }

            // My Reservations
            ExtendedFloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 30.dp),
                onClick = { onMyReservations() }
            ) {
                Text("My Reservations")
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowEntrantHomeScreenPreview() {
    EntrantHomeScreen(emptyList(), { } )
}