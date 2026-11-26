package com.example.magpie_pinecone.frontend.entrantScreens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.entrantClasses.Entrant
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.backend.lotteryClasses.LotteryDB
import com.example.magpie_pinecone.frontend.sharedComposables.NotifsInbox
import com.example.magpie_pinecone.ui.components.CampsiteImage
import com.example.magpie_pinecone.ui.components.PillButton
import com.example.magpie_pinecone.ui.components.StarRating
import com.example.magpie_pinecone.ui.theme.LavenderSurface
import com.example.magpie_pinecone.ui.theme.LeaveRed
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import com.example.magpie_pinecone.ui.theme.OutlineLavender
import com.example.magpie_pinecone.ui.theme.PineconePurple
import com.example.magpie_pinecone.ui.theme.ReserveGreen

/*
 * EntrantHomeScreen.kt contains all relevant functions for displaying the entrant's home screen.
 *
 * Composable Functions:
 * EntrantHomeScreen displays the entrant home screen (landing page).
 * HomeWelcomeBanner displays the welcome banner at the top of the entrant home screen.
 * EntrantHomeScreenLotteryCard displays a single card containing the entrant's entry in a campsite lottery.
 * WinnerActions displays within a lottery card a "Reserve" button for when the entrant wins a lottery.
 * LotteryEntryActions displays within a lottery card "Details" and "Leave" buttons for when the entrant has entered an active lottery.
 * ShowEntrantHomeScreenPreview displays a preview of EntrantHomeScreen for reference.
 */

/**
 * Displays the entrant landing page.
 *
 * The home screen shows the entrant's active lottery entries and whether
 * they have won each lottery. Entrants can view campsite details, leave
 * lotteries, reserve a campsite after winning, or open their reservations.
 *
 * @param onMyReservations Opens the entrant's reservations.
 * @param onNotifications Opens the entrant notifications screen.
 * @param lotteries Lotteries that the entrant has joined.
 * @param curEntrant The entrant currently using the app.
 * @param onReserveLottery Called when a lottery winner chooses to reserve.
 * @param onLotteryDetails Opens the details for a lottery's campsite.
 * @param onLeaveLottery Removes the entrant from the selected lottery.
 */
@Composable
fun EntrantHomeScreen(
    onMyReservations: () -> Unit,
    onNotifications: () -> Unit,
    lotteryDB: LotteryDB,
//    lotteries: List<Lottery>,
    curEntrant: Entrant?,
    onReserveLottery: (lottery: Lottery) -> Unit,
    onLotteryDetails: (lottery: Lottery) -> Unit,
    onLeaveLottery: (lottery: Lottery) -> Unit
) {


    var lotteries by remember {mutableStateOf<List<Lottery>>(emptyList())}

    if (curEntrant == null) {
        return
    }

    // gets the lotteries on refresh from the database now to show accurate lotteries joined and left
    LaunchedEffect(curEntrant?.id) {
        curEntrant.id.let { id ->
            lotteryDB.getLotteriesByEntrant(id) { lotteries = it }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HomeWelcomeBanner(
                entrantName = curEntrant.name
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 12.dp,
                        top = 16.dp,
                        bottom = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Lottery Entries",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )

                /*
                 * Notification functionality is implemented separately.
                 * Reuse the shared inbox component here rather than
                 * duplicating notification behaviour in the home screen.
                 */
                NotifsInbox(
                    onInboxClicked = onNotifications
                )
            }

            if (lotteries.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "You have no lottery entries yet.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 4.dp,
                        bottom = 96.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(lotteries) { lottery ->
                        val campsite = lottery.campsites.firstOrNull()

                        /*
                         * A lottery should normally contain a campsite.
                         * Avoid indexing campsites[0] so malformed data
                         * cannot crash the entire home screen.
                         */
                        if (campsite != null) {

                                EntrantHomeScreenLotteryCard(
                                    wonLottery = lottery.winners.any {
                                        it.id == curEntrant.id
                                    },
                                    lottery = lottery,
                                    campsite = campsite,
                                    onReserveLottery = {
                                        onReserveLottery(lottery)
                                    },
                                    onLotteryDetails = {
                                        onLotteryDetails(lottery)
                                    },
                                    onLeaveLottery = {
                                        onLeaveLottery(lottery)
                                        lotteries = lotteries - lottery
                                    }
                                )

                        }
                    }
                }
            }
        }

        ExtendedFloatingActionButton(
            onClick = onMyReservations,
            containerColor = PineconePurple,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 20.dp,
                    bottom = 18.dp
                )
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = "My reservations",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Purple welcome banner displayed at the top of the entrant home screen.
 *
 * @param entrantName Name of the currently signed-in entrant.
 */
@Composable
private fun HomeWelcomeBanner(
    entrantName: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(PineconePurple)
            .padding(
                horizontal = 28.dp,
                vertical = 26.dp
            )
    ) {
        Text(
            text = "Welcome back,\n${entrantName.ifBlank { "User" }}!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Color.White
        )
    }
}

/**
 * Displays one lottery entry on the entrant home screen.
 *
 * Winners see a reservation action. Entrants who have not won can view
 * the campsite details or leave the lottery.
 *
 * @param wonLottery Whether the current entrant won this lottery.
 * @param lottery Lottery represented by the card.
 * @param campsite Campsite displayed by this lottery entry.
 * @param onReserveLottery Called when the winner selects Reserve.
 * @param onLotteryDetails Called when Details is selected.
 * @param onLeaveLottery Called when Leave is selected.
 */
@Composable
fun EntrantHomeScreenLotteryCard(
    wonLottery: Boolean,
    lottery: Lottery,
    campsite: Campsite,
    onReserveLottery: () -> Unit,
    onLotteryDetails: () -> Unit,
    onLeaveLottery: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = OutlineLavender
        ),
        colors = CardDefaults.cardColors(
            containerColor = LavenderSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 12.dp,
                        top = 12.dp,
                        bottom = 10.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(116.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = campsite.name.ifBlank {
                            "Unnamed campsite"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    StarRating(
                        rating = campsite.rating
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.People,
                            contentDescription = "Maximum occupancy",
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text(
                            text = campsite.maxOccupancy.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "$${formatPrice(campsite.price)}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Text(
                        text = campsite.location,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                CampsiteImage(
                    modifier = Modifier
                        .width(148.dp)
                        .height(116.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            if (wonLottery) {
                WinnerActions(
                    onReserveLottery = onReserveLottery
                )
            } else {
                LotteryEntryActions(
                    onLotteryDetails = onLotteryDetails,
                    onLeaveLottery = onLeaveLottery
                )
            }
        }
    }
}

/**
 * Bottom section displayed when the entrant has won a lottery.
 */
@Composable
private fun WinnerActions(
    onReserveLottery: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 18.dp,
                end = 12.dp,
                bottom = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "You won the lottery!",
            style = MaterialTheme.typography.bodyMedium,
            color = ReserveGreen,
            modifier = Modifier.weight(1f)
        )

        PillButton(
            text = "Reserve",
            onClick = onReserveLottery,
            containerColor = ReserveGreen
        )
    }
}

/**
 * Joined Details and Leave controls shown for an active lottery entry.
 */
@Composable
private fun LotteryEntryActions(
    onLotteryDetails: () -> Unit,
    onLeaveLottery: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 12.dp,
                end = 12.dp,
                bottom = 12.dp
            )
    ) {
        Button(
            onClick = onLotteryDetails,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(
                topStart = 24.dp,
                bottomStart = 24.dp,
                topEnd = 0.dp,
                bottomEnd = 0.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = BorderStroke(
                width = 1.dp,
                color = OutlineLavender
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp
            )
        ) {
            Text(
                text = "Details",
                fontWeight = FontWeight.SemiBold
            )
        }

        Button(
            onClick = onLeaveLottery,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                bottomStart = 0.dp,
                topEnd = 24.dp,
                bottomEnd = 24.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = LeaveRed,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp
            )
        ) {
            Text(
                text = "Leave",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Formats prices without unnecessary decimal places.
 */
private fun formatPrice(
    price: Double
): String {
    return if (price % 1.0 == 0.0) {
        price.toInt().toString()
    } else {
        "%.2f".format(price)
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun EntrantHomeScreenPreview() {
    val entrant = Entrant(
        id = "entrant_101",
        name = "User",
        email = "user@example.com",
        notifOptOut = false
    )

    val campsite = Campsite(
        ID = "campsite_1",
        name = "Campsite",
        location = "1234 567 St",
        maxOccupancy = 5,
        price = 38.0,
        rating = 5.0
    )

    val activeLottery = Lottery(
        id = "lottery_1",
        campsites = mutableListOf(campsite),
        waitlist = mutableListOf(entrant)
    )

    val wonLottery = Lottery(
        id = "lottery_2",
        campsites = mutableListOf(campsite),
        waitlist = mutableListOf(entrant),
        winners = listOf(entrant)
    )

    MagpiepineconeTheme {
        EntrantHomeScreen(
            onMyReservations = {},
            onNotifications = {},
            lotteryDB = LotteryDB(),
//            lotteries = listOf(
//                activeLottery,
//                wonLottery,
//                activeLottery
//            ),
            curEntrant = entrant,
            onReserveLottery = {},
            onLotteryDetails = {},
            onLeaveLottery = {}
        )
    }
}