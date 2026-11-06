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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magpie_pinecone.EditLottery
import com.example.magpie_pinecone.R
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.entrantClasses.Entrant
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import java.text.SimpleDateFormat
import java.util.Locale

 /**
 * Display's Entrant's Home Screen
 * TO-DO
 * Link to entrant's actual lotteries to loop over
 * Implement check for if in lottery or not to switch between buttons
 * Implement links to Notifs (after halfway point), Details, Leave, Reserve, My Reservations buttons
 */

// TODO: ALSO TEMPORARY
private fun parseDate(dateStr: String) = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateStr)

@Composable
fun EntrantHomeScreen(
    onMyReservations: () -> Unit,
    lotteries: List<Lottery>,
) {

    // TODO: THIS IS TEMPORARY!!! USED FOR TESTING
    val sampleLotteries = listOf(
        // 1. High-demand backcountry peak summer lottery
        Lottery(
            id = "lottery_lake_ohara_summer",
            campsites = mutableListOf(
                Campsite(ID = "site_ohara_01", name = "Lake O'Hara Backcountry A", price=123.0, location="hello ave"),
                Campsite(ID = "site_ohara_02", name = "Lake O'Hara Backcountry B")
            ),
            registrationStartDate = parseDate("2026-02-01"),
            registrationEndDate = parseDate("2026-02-28"),
            bookingStartDate = parseDate("2026-03-05"),
            bookingEndDate = parseDate("2026-03-12"),
            activeStartDate = parseDate("2026-07-01"),
            activeEndDate = parseDate("2026-08-31"),
            numberOfWinners = 15,
            waitlistCapacity = 250,
            waitlist = mutableListOf(
                Entrant(id = "entrant_101", name = "Alex Chen", email = "alex.chen@example.com", notifOptOut = false),
                Entrant(id = "entrant_102", name = "Maya Patel", email = "maya.patel@example.com", notifOptOut = false),
                Entrant(id = "entrant_103", name = "Liam Vance", email = "liam.v@example.com", notifOptOut = false)
            )
        ),

        // 2. Popular riverside drive-in campground lottery
        Lottery(
            id = "lottery_two_jack_main",
            campsites = mutableListOf(
                Campsite(ID = "site_tj_12", name = "Two Jack Lakeside - Site 12", price = 102.0, location = "1234 St"),
            ),
            registrationStartDate = parseDate("2026-03-15"),
            registrationEndDate = parseDate("2026-04-15"),
            bookingStartDate = parseDate("2026-04-20"),
            bookingEndDate = parseDate("2026-04-27"),
            activeStartDate = parseDate("2026-06-15"),
            activeEndDate = parseDate("2026-09-15"),
            numberOfWinners = 25,
            waitlistCapacity = 500,
            waitlist = mutableListOf(
                Entrant(id = "entrant_201", name = "Sarah Jenkins", email = "sjenkins@example.com", notifOptOut = false),
                Entrant(id = "entrant_202", name = "Tariq Morales", email = "tmorales@example.com", notifOptOut = false)
            )
        ),

        // 3. Fall shoulder season group site lottery
        Lottery(
            id = "lottery_point_wolf_fall",
            campsites = mutableListOf(
                Campsite(ID = "site_pw_grp1", name = "Point Wolfe Group Site Alpha", location="Point Wolfe, OH")
            ),
            registrationStartDate = parseDate("2026-05-01"),
            registrationEndDate = parseDate("2026-05-31"),
            bookingStartDate = parseDate("2026-06-05"),
            bookingEndDate = parseDate("2026-06-12"),
            activeStartDate = parseDate("2026-09-18"),
            activeEndDate = parseDate("2026-10-15"),
            numberOfWinners = 5,
            waitlistCapacity = 75,
            waitlist = mutableListOf(
                Entrant(id = "entrant_301", name = "Chloe Dubois", email = "chloe.d@example.com", notifOptOut = false)
            )
        )
    )
    // END OF TEMPORARY DATA

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
                    items(sampleLotteries) { lottery ->
                        EntrantHomeScreenLotteryCard(wonLottery = true, lottery = lottery)
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

@Composable
fun EntrantHomeScreenLotteryCard(wonLottery: Boolean, lottery: Lottery, modifier: Modifier = Modifier) {
  // TODO: SUPPORT MULTIPLE CAMPSITES PER LOTTERY
    Card(
        modifier = Modifier.fillMaxWidth().height(240.dp).padding(5.dp)
    ) {
        Box() {
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().weight(2f),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Campsite Details
                    Column(modifier = Modifier.weight(1.5f)) {
                        // This basic text component will resize to fit the campsite's name, and adds ellipsis if it doesn't fit
                        BasicText(
                            text = lottery.campsites[0].name,
                            maxLines = 1, // Forces the text to stay on a single line
                            overflow = TextOverflow.Ellipsis, // Backup handling if it doesn't fit the min bounds
                            //style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                            autoSize = TextAutoSize.StepBased(
                                minFontSize = 24.sp,  // The absolute smallest the text can shrink to
                                maxFontSize = 32.sp,  // The starting target font size
                                stepSize = 1.sp       // The increments it drops by when calculating fit
                            )
                        )

                        Text(
                            text = "Rating: " + lottery.campsites[0].rating.toString(),
                            fontSize = 16.sp
                        )

                        Text(
                            text = "Max Capacity: " + lottery.campsites[0].maxOccupancy,
                            fontSize = 16.sp
                        )

                        Text(
                            text = "\$${lottery.campsites[0].price} / night",
                            fontSize = 16.sp
                        )

                        Text(
                            text = lottery.campsites[0].location,
                            fontSize = 16.sp
                        )
                    }

                    // TODO: ADD IMAGE FROM CAMPSITE CLASS
                    // Placeholder Icon Image
                    Image(
                        modifier = Modifier.weight(1f),
                        painter = painterResource(id = R.drawable.placeholdercampsiteimage),
                        contentDescription = "Campsite Image",
                        contentScale = ContentScale.Crop
                    )
                }

                if (wonLottery) {
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
    EntrantHomeScreen({})
}