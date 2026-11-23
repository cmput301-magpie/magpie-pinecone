package com.example.magpie_pinecone.frontend.ownerScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.ui.components.CampsiteImage
import com.example.magpie_pinecone.ui.components.StarRating
import com.example.magpie_pinecone.ui.theme.LavenderSurface
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import com.example.magpie_pinecone.ui.theme.OutlineLavender
import com.example.magpie_pinecone.ui.theme.PineconePurple
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/*
 * OwnerHomeScreen.kt contains all relevant functions for displaying the campsite owner's home screen.
 *
 * Composable Functions:
 * OwnerHomeScreen displays the owner home screen (landing page).
 * OwnerWelcomeBanner displays the purple welcome banner at the top of the screen.
 * MyLotteries displays the list of lotteries belonging to the owner.
 * LotteryCard displays one of the owner's lotteries.
 * MyCampsites displays the list of campsites belonging to the owner.
 * CampsiteCard displays one of the owner's campsites.
 * CardMenuButton displays the white "..." button that opens a card's menu.
 * EmptyListMessage displays a grey message when a tab has nothing to show.
 * OwnerActionButton displays a purple floating button with an icon, like "Campsite".
 * ShowOwnerHomeScreenPreview displays a preview of OwnerHomeScreen for reference.
 */

/**
 * Displays the campsite owner's landing page.
 *
 * Owners switch between their lotteries and their campsites with the
 * segmented control under the banner. Each card has a "..." menu with
 * the actions for that lottery or campsite.
 *
 * @param campsites Campsites owned by the owner.
 * @param lotteries Lotteries created by the owner.
 * @param onEditLottery Opens the selected lottery for editing.
 * @param onCreateLottery Opens the screen to start a new lottery.
 * @param onViewWaitlist Opens the waiting list of the selected lottery.
 * @param onEditCampsite Opens the selected campsite for editing.
 * @param onCreateCampsite Opens the screen to register a new campsite.
 * @param onPreviewCampsite Opens a preview of the selected campsite.
 * @param onDatesBooked Opens the booked dates of the selected campsite.
 * @param onViewReservations Opens the owner's campsite reservations.
 * @param onQRCode Opens the QR code of the selected campsite.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerHomeScreen(
    campsites: List<Campsite>,
    lotteries: List<Lottery>,
    onEditLottery: (Lottery) -> Unit,
    onCreateLottery: () -> Unit,
    onViewWaitlist: (Lottery) -> Unit,
    onEditCampsite: (Campsite) -> Unit,
    onCreateCampsite: () -> Unit,
    onPreviewCampsite: (Campsite) -> Unit,
    onDatesBooked: (Campsite) -> Unit,
    onViewReservations: () -> Unit,
    onQRCode: (Campsite) -> Unit
) {
    // Tab Navigation (My Campsites opens first)
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(1) }
    val tabs = listOf("My Lotteries", "My Campsites")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OwnerWelcomeBanner()

        // Rounded two-part switch between lotteries and campsites, like Figma
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.padding(
                horizontal = 40.dp,
                vertical = 16.dp
            )
        ) {
            tabs.forEachIndexed { index, label ->
                SegmentedButton(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = tabs.size
                    ),
                    icon = {}   // No checkmark, like Figma
                ) {
                    Text(label)
                }
            }
        }

        // Body, depending on the tab. weight(1f) gives it the rest of the screen.
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (selectedTabIndex) {
                0 -> MyLotteries(
                    lotteries,
                    onEditLottery,
                    onCreateLottery,
                    onViewWaitlist
                )
                1 -> MyCampsites(
                    campsites,
                    onEditCampsite,
                    onCreateCampsite,
                    onPreviewCampsite,
                    onDatesBooked,
                    onViewReservations,
                    onQRCode
                )
            }
        }
    }
}

/**
 * Purple welcome banner at the top of the owner home screen.
 * Same style as the banner on the entrant home screen.
 */
@Composable
private fun OwnerWelcomeBanner() {
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
            text = "Welcome back,\nOwner!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Color.White
        )
    }
}

/**
 * Displays the owner's lotteries, with a button to start a new one.
 *
 * @param lotteries Lotteries created by the owner.
 * @param onEditLottery Opens the selected lottery for editing.
 * @param onCreateLottery Opens the screen to start a new lottery.
 * @param onViewWaitlist Opens the waiting list of the selected lottery.
 */
@Composable
fun MyLotteries(
    lotteries: List<Lottery>,
    onEditLottery: (Lottery) -> Unit,
    onCreateLottery: () -> Unit,
    onViewWaitlist: (Lottery) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (lotteries.isEmpty()) {
            EmptyListMessage(
                text = "You have no lotteries yet."
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                // Extra space at the bottom so the last card is not hidden behind the button
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 96.dp
                )
            ) {
                items(lotteries) { lottery ->
                    LotteryCard(
                        lottery = lottery,
                        menuItems = listOf(
                            "Edit" to { onEditLottery(lottery) },
                            "View waitlist" to { onViewWaitlist(lottery) }
                        )
                    )
                }
            }
        }

        OwnerActionButton(
            text = "Lottery",
            icon = Icons.Outlined.AddCircleOutline,
            onClick = onCreateLottery,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 20.dp,
                    bottom = 18.dp
                )
        )
    }
}

/**
 * Displays one of the owner's lotteries.
 *
 * A lottery has no name of its own, so the card is titled with the names
 * of its campsites and shows the first campsite's picture.
 *
 * @param lottery Lottery shown on the card.
 * @param menuItems Actions in the card's "..." menu, as (label, action) pairs.
 */
@Composable
fun LotteryCard(
    lottery: Lottery,
    menuItems: List<Pair<String, () -> Unit>>
) {
    // A lottery has no name of its own, so title it using its campsites
    val title = lottery.campsites
        .map { it.name.ifBlank { "Unnamed campsite" } }
        .joinToString(", ")
        .ifBlank { "Unnamed lottery" }

    OwnerCard(
        menuItems = menuItems
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "Registration ends ${formatDate(lottery.registrationEndDate)}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Winners: ${lottery.numberOfWinners}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Waitlist capacity: ${lottery.waitlistCapacity}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Displays the owner's campsites, with buttons to register a new campsite
 * and to view campsite reservations.
 *
 * @param campsites Campsites owned by the owner.
 * @param onEditCampsite Opens the selected campsite for editing.
 * @param onCreateCampsite Opens the screen to register a new campsite.
 * @param onPreviewCampsite Opens a preview of the selected campsite.
 * @param onDatesBooked Opens the booked dates of the selected campsite.
 * @param onViewReservations Opens the owner's campsite reservations.
 * @param onQRCode Opens the QR code of the selected campsite.
 */
@Composable
fun MyCampsites(
    campsites: List<Campsite>,
    onEditCampsite: (Campsite) -> Unit,
    onCreateCampsite: () -> Unit,
    onPreviewCampsite: (Campsite) -> Unit,
    onDatesBooked: (Campsite) -> Unit,
    onViewReservations: () -> Unit,
    onQRCode: (Campsite) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (campsites.isEmpty()) {
            EmptyListMessage(
                text = "You have no campsites yet."
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                // Extra space at the bottom so the last card is not hidden behind the buttons
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 160.dp
                )
            ) {
                // For each campsite, display a card with the details
                items(campsites) { campsite ->
                    CampsiteCard(
                        campsite = campsite,
                        menuItems = listOf(
                            "Edit" to { onEditCampsite(campsite) },
                            "Preview" to { onPreviewCampsite(campsite) },
                            "Dates Booked" to { onDatesBooked(campsite) },
                            "Get QR Code" to { onQRCode(campsite) }
                        )
                    )
                }
            }
        }

        // Floating buttons to add a campsite and view campsite reservations
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 20.dp,
                    bottom = 18.dp
                ),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OwnerActionButton(
                text = "Campsite",
                icon = Icons.Outlined.AddCircleOutline,
                onClick = onCreateCampsite
            )
            OwnerActionButton(
                text = "Campsite Resv.",
                icon = Icons.Filled.Star,
                onClick = onViewReservations
            )
        }
    }
}

/**
 * Displays one of the owner's campsites: name, rating, occupancy, price
 * and address on the left, picture on the right.
 *
 * @param campsite Campsite shown on the card.
 * @param menuItems Actions in the card's "..." menu, as (label, action) pairs.
 */
@Composable
fun CampsiteCard(
    campsite: Campsite,
    menuItems: List<Pair<String, () -> Unit>>
) {
    OwnerCard(
        menuItems = menuItems
    ) {
        Text(
            text = campsite.name.ifBlank { "Unnamed campsite" },
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        StarRating(
            rating = campsite.rating
        )

        // Occupancy on the left, price on the right
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.People,
                contentDescription = "Maximum occupancy",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = campsite.maxOccupancy.toString(),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "\$${formatPrice(campsite.price)}",
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
}

/**
 * Shared card layout for lottery and campsite cards: text on the left,
 * picture on the right with the "..." menu button on top of it.
 * Same look as the cards on the entrant home screen.
 *
 * @param menuItems Actions in the card's "..." menu, as (label, action) pairs.
 * @param details The text shown on the left side of the card.
 */
@Composable
private fun OwnerCard(
    menuItems: List<Pair<String, () -> Unit>>,
    details: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
        Row(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 12.dp,
                top = 12.dp,
                bottom = 12.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(116.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                details()
            }

            // Picture with the "..." button in its top-right corner
            Box(
                modifier = Modifier
                    .width(148.dp)
                    .height(116.dp)
            ) {
                CampsiteImage(
                    modifier = Modifier.fillMaxSize()
                )
                CardMenuButton(
                    menuItems = menuItems,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                )
            }
        }
    }
}

/**
 * Small white round "..." button that opens a dropdown menu.
 *
 * @param menuItems Actions in the menu, as (label, action) pairs.
 * @param modifier Places the button on the card.
 */
@Composable
private fun CardMenuButton(
    menuItems: List<Pair<String, () -> Unit>>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }  // Start dropdown menu collapsed

    Box(
        modifier = modifier
    ) {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.size(32.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Icon(
                imageVector = Icons.Filled.MoreHoriz,
                contentDescription = "More options",
                modifier = Modifier.size(20.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            menuItems.forEach { (title, action) ->
                DropdownMenuItem(
                    text = { Text(title) },
                    onClick = {
                        expanded = false
                        action()
                    }
                )
            }
        }
    }
}

/**
 * Grey message shown near the top of a tab that has nothing to show.
 *
 * @param text Message to show.
 */
@Composable
private fun EmptyListMessage(
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Purple floating button with an icon and a label, like "Campsite".
 * Same colours as the "My reservations" button on the entrant home screen.
 *
 * @param text Label of the button.
 * @param icon Icon shown before the label.
 * @param onClick Called when the button is pressed.
 * @param modifier Places the button on the screen.
 */
@Composable
private fun OwnerActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = PineconePurple,
        contentColor = Color.White,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * Formats a date as MM/dd/yyyy, or "TBD" if the date has not been set.
 */
private fun formatDate(
    date: Date?
): String {
    return date?.let { SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(it) } ?: "TBD"
}

/**
 * Formats prices without unnecessary decimal places (38.0 becomes "38").
 * Same rule as the entrant screens, so prices look the same everywhere.
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
fun ShowOwnerHomeScreenPreview() {
    MagpiepineconeTheme {
        OwnerHomeScreen(
            lotteries = emptyList(),
            campsites = listOf(
                Campsite(
                    ID = "campsite_1",
                    name = "Campsite",
                    location = "1234 567 St",
                    maxOccupancy = 5,
                    price = 38.0,
                    rating = 5.0
                )
            ),
            onEditLottery = {},
            onCreateLottery = {},
            onViewWaitlist = {},
            onEditCampsite = {},
            onCreateCampsite = {},
            onPreviewCampsite = {},
            onDatesBooked = {},
            onViewReservations = {},
            onQRCode = {}
        )
    }
}