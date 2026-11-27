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
import androidx.compose.foundation.layout.Row            
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import com.example.magpie_pinecone.ui.theme.DeclineRed

/*
 * ViewReservationsScreen.kt contains all relevant functions for displaying the owner's campsite reservations.
 *
 * Composable Functions:
 * ViewReservationsScreen displays the owner's reservations, filtering by active, past and cancelled reservations.
 * ReservationTab displays one tab of the tab bar, with its icon and name.
 * ActiveReservations displays the owner's active reservations.
 * PastReservations displays the owner's past reservations.
 * CancelledReservations displays the owner's cancelled reservations.
 * OwnerReservationList displays a list of reservations, used by all three tabs.
 * OwnerReservationRow displays one reservation's campsite name and dates, with an optional Reject button.
 * EmptyReservationsMessage displays a grey message when a tab has no reservations.
 * ShowViewReservationsScreenPreview displays a preview of ViewReservationsScreen for reference.
 */

/**
 * Displays the reservations for the owner's campsites, split into
 * Active, Past and Cancelled tabs.
 *
 * @param activeReservations The owner's active reservations.
 * @param pastReservations The owner's past reservations.
 * @param cancelledReservations The owner's cancelled and rejected reservations.
 * @param campsiteDB Database used to look up each reservation's campsite.
 * @param onReject Called when the owner confirms rejecting an active reservation.
 * @param onBack Returns to the previous screen.
 */
@Composable
fun ViewReservationsScreen(
    activeReservations: List<Reservation>,
    pastReservations: List<Reservation> = emptyList(),
    cancelledReservations: List<Reservation> = emptyList(),
    campsiteDB: CampsiteDB,
    onReject: (Reservation) -> Unit = {},
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
            0 -> ActiveReservations(activeReservations, campsiteDB, onReject)
            1 -> PastReservations(pastReservations, campsiteDB)
            2 -> CancelledReservations(cancelledReservations, campsiteDB)
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
 * Displays the owner's active reservations, each with a Reject button (US 02.08.01).
 *
 * @param activeReservations The owner's active reservations.
 * @param campsiteDB Database used to look up each reservation's campsite.
 * @param onReject Called when the owner confirms rejecting a reservation.
 */
@Composable
fun ActiveReservations(
    activeReservations: List<Reservation>,
    campsiteDB: CampsiteDB,
    onReject: (Reservation) -> Unit
) {
    OwnerReservationList(
        reservations = activeReservations,
        campsiteDB = campsiteDB,
        emptyText = "You have no active reservations.",
        onReject = onReject
    )
}

/**
 * Displays the owner's past reservations (US 02.07.02).
 *
 * @param pastReservations The owner's past reservations.
 * @param campsiteDB Database used to look up each reservation's campsite.
 */
@Composable
fun PastReservations(
    pastReservations: List<Reservation>,
    campsiteDB: CampsiteDB
) {
    OwnerReservationList(
        reservations = pastReservations,
        campsiteDB = campsiteDB,
        emptyText = "You have no past reservations."
    )
}

/**
 * Displays the owner's cancelled and rejected reservations (US 02.07.03).
 *
 * @param cancelledReservations The owner's cancelled reservations.
 * @param campsiteDB Database used to look up each reservation's campsite.
 */
@Composable
fun CancelledReservations(
    cancelledReservations: List<Reservation>,
    campsiteDB: CampsiteDB
) {
    OwnerReservationList(
        reservations = cancelledReservations,
        campsiteDB = campsiteDB,
        emptyText = "You have no cancelled reservations."
    )
}

/**
 * Shows a list of reservations in one rounded card.
 * Used by all three tabs, so the list code is only written once.
 *
 * @param reservations Reservations to show.
 * @param campsiteDB Database used to look up each reservation's campsite.
 * @param emptyText Message shown when there are no reservations.
 * @param onReject If not null, each row gets a Reject button that calls this after the owner confirms.
 */
@Composable
private fun OwnerReservationList(
    reservations: List<Reservation>,
    campsiteDB: CampsiteDB,
    emptyText: String,
    onReject: ((Reservation) -> Unit)? = null
) {
    // Campsites we have looked up so far, by campsite ID
    var campsites by remember { mutableStateOf<Map<String, Campsite>>(emptyMap()) }
    // The reservation the owner picked to reject (null = none picked)
    var reservationToReject by remember { mutableStateOf<Reservation?>(null) }

    // Look up each campsite once (not on every redraw), so rows can show its name
    LaunchedEffect(reservations) {
        reservations
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

    if (reservations.isEmpty()) {
        EmptyReservationsMessage(
            text = emptyText
        )
    } else {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            LazyColumn {
                itemsIndexed(reservations) { index, reservation ->
                    // A thin line between rows, but not above the first one
                    if (index > 0) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }

                    OwnerReservationRow(
                        reservation = reservation,
                        campsiteName = campsites[reservation.campsiteID]?.name,
                        showRejectButton = onReject != null,
                        onReject = { reservationToReject = reservation }
                    )
                }
            }
        }
    }

    // Ask the owner to confirm before rejecting
    reservationToReject?.let { toReject ->
        AlertDialog(
            onDismissRequest = { reservationToReject = null },
            title = { Text("Reject reservation?") },
            text = { Text("The entrant will lose this reservation.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onReject?.invoke(toReject)
                        reservationToReject = null
                    }
                ) {
                    Text("Yes, reject")
                }
            },
            dismissButton = {
                TextButton(onClick = { reservationToReject = null }) {
                    Text("Keep it")
                }
            }
        )
    }
}

/**
 * One reservation row: the campsite's name with the dates in grey under it,
 * and a red Reject button on the right if [showRejectButton] is true.
 *
 * @param reservation Reservation shown in this row.
 * @param campsiteName Name of the reservation's campsite, or null while it is loading.
 * @param showRejectButton Whether to show the Reject button.
 * @param onReject Called when the owner presses Reject.
 */
@Composable
private fun OwnerReservationRow(
    reservation: Reservation,
    campsiteName: String?,
    showRejectButton: Boolean = false,
    onReject: () -> Unit = {}
) {
    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 8.dp,
                top = 12.dp,
                bottom = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Name and dates take up all the space the button doesn't need
        Column(
            modifier = Modifier.weight(1f)
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

        if (showRejectButton) {
            TextButton(
                onClick = onReject,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = DeclineRed
                )
            ) {
                Text("Reject")
            }
        }
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