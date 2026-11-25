package com.example.magpie_pinecone.frontend.ownerScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.campsiteClasses.CampsiteDB
import com.example.magpie_pinecone.backend.reservationClasses.Reservation
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import java.text.SimpleDateFormat
import java.util.Locale

/*
 * ViewReservationsScreen.kt contains all relevant functions for displaying the owner's campsite reservations.
 *
 * Composable Functions:
 * ViewReservationsScreen displays the owner's reservations, filtering by active, past and cancelled reservations.
 * ReservationTab displays one tab of the tab bar, with its icon and name.
 * ActiveReservations displays the owner's active reservations.
 * PastReservations displays the owner's past reservations.
 * CancelledReservations displays the owner's cancelled reservations.
 * OwnerReservationRow displays one reservation's campsite name and dates.
 * EmptyReservationsMessage displays a grey message when a tab has no reservations.
 * ShowViewReservationsScreenPreview displays a preview of ViewReservationsScreen for reference.
 */

/**
 * Displays the reservations for the owner's campsites, split into
 * Active, Past and Cancelled tabs.
 *
 * @param activeReservations The owner's active reservations.
 * @param campsiteDB Database used to look up each reservation's campsite.
 * @param onBack Returns to the previous screen.
 */
@Composable
fun ViewReservationsScreen(
    activeReservations: List<Reservation>,
    campsiteDB: CampsiteDB,
    onBack: () -> Unit,
) {
    // Tab Navigation
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

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
            title = "View Reservations",
            onBack = onBack
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Lavender tab bar with an icon on each tab, like Figma
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) {
            ReservationTab(
                title = "Active",
                icon = Icons.Outlined.CalendarMonth,
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 }
            )
            ReservationTab(
                title = "Past",
                icon = Icons.Outlined.Schedule,
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 }
            )
            ReservationTab(
                title = "Cancelled",
                icon = Icons.Outlined.Close,
                selected = selectedTabIndex == 2,
                onClick = { selectedTabIndex = 2 }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Body, depending on the tab
        when (selectedTabIndex) {
            0 -> ActiveReservations(activeReservations, campsiteDB)
            1 -> PastReservations()
            2 -> CancelledReservations()
        }
    }
}

/**
 * One tab of the tab bar: an icon above its name.
 *
 * @param title Name of the tab.
 * @param icon Icon shown above the name.
 * @param selected Whether this tab is the one being shown.
 * @param onClick Called when the tab is tapped.
 */
@Composable
private fun ReservationTab(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Tab(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        text = { Text(title) }
    )
}

/**
 * Displays the owner's active reservations in one rounded card.
 *
 * @param activeReservations The owner's active reservations.
 * @param campsiteDB Database used to look up each reservation's campsite.
 */
@Composable
fun ActiveReservations(
    activeReservations: List<Reservation>,
    campsiteDB: CampsiteDB
) {
    // Campsites we have looked up so far, by campsite ID
    var campsites by remember { mutableStateOf<Map<String, Campsite>>(emptyMap()) }

    // Look up each campsite once (not on every redraw), so rows can show its name
    LaunchedEffect(activeReservations) {
        activeReservations
            .map { it.campsiteID }
            .distinct()
            .filter { it !in campsites }
            .forEach { campsiteId ->
                campsiteDB.readCampsite(campsiteId) { campsite ->
                    if (campsite != null) {
                        campsites = campsites + (campsiteId to campsite)
                    }
                }
            }
    }

    if (activeReservations.isEmpty()) {
        EmptyReservationsMessage(
            text = "You have no active reservations."
        )
    } else {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            LazyColumn {
                itemsIndexed(activeReservations) { index, reservation ->
                    // A thin line between rows, but not above the first one
                    if (index > 0) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }

                    OwnerReservationRow(
                        reservation = reservation,
                        campsiteName = campsites[reservation.campsiteID]?.name
                    )
                }
            }
        }
    }
}

/**
 * Displays the owner's past reservations.
 *
 * Loading real past reservations is its own user story (US 02.07.02),
 * so this tab only shows a message for now.
 */
@Composable
fun PastReservations() {
    EmptyReservationsMessage(
        text = "Past reservations will appear here."
    )
}

/**
 * Displays the owner's cancelled reservations.
 *
 * Loading real cancelled reservations is its own user story (US 02.07.03),
 * so this tab only shows a message for now.
 */
@Composable
fun CancelledReservations() {
    EmptyReservationsMessage(
        text = "Cancelled reservations will appear here."
    )
}

/**
 * One reservation row: the campsite's name, with the dates in grey under it.
 *
 * @param reservation Reservation shown in this row.
 * @param campsiteName Name of the reservation's campsite, or null while it is loading.
 */
@Composable
private fun OwnerReservationRow(
    reservation: Reservation,
    campsiteName: String?
) {
    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
    ) {
        Text(
            text = campsiteName ?: "Loading campsite...",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${dateFormat.format(reservation.startDate)} - ${dateFormat.format(reservation.endDate)}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Grey message shown near the top of a tab that has no reservations.
 *
 * @param text Message to show.
 */
@Composable
private fun EmptyReservationsMessage(
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

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ShowViewReservationsScreenPreview() {
    MagpiepineconeTheme {
        ViewReservationsScreen(
            activeReservations = listOf(),
            campsiteDB = CampsiteDB(),
            onBack = { }
        )
    }
}