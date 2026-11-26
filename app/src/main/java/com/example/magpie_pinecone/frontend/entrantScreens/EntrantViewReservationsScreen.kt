package com.example.magpie_pinecone.frontend.entrantScreens

import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.campsiteClasses.CampsiteDB
import com.example.magpie_pinecone.backend.reservationClasses.Reservation
import com.example.magpie_pinecone.backend.reservationClasses.ReservationDB
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.example.magpie_pinecone.ui.theme.DeclineRed
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import java.text.SimpleDateFormat
import java.util.Locale

/*
 * EntrantViewReservationsScreen.kt contains all relevant functions for displaying the entrant's reservations.
 *
 * Composable Functions:
 * EntrantViewReservationsScreen displays the entrant's reservations, filtering by active and past reservations, and with an option to cancel active reservations.
 * ActiveEntrantReservations displays the entrant's active reservations, with an option to cancel.
 * PastEntrantReservations displays the entrant's past reservations.
 * ReservationRow displays one reservation's campsite name and dates, with a Cancel button.
 * EmptyReservationsMessage displays a grey message when a tab has no reservations.
 * ShowEntrantViewReservationsScreenPreview displays a preview of EntrantViewReservationsScreen for reference.
 */

/**
 * Displays the entrant's reservations, split into Active and Past tabs.
 *
 * @param activeReservations The entrant's active reservations.
 * @param campsiteDB Database used to look up each reservation's campsite.
 * @param onCancelled Called after a reservation is cancelled, with the cancelled campsite if it was found.
 * @param onBack Returns to the previous screen.
 */
@Composable
fun EntrantViewReservationsScreen(
    activeReservations: List<Reservation>,
    campsiteDB: CampsiteDB,
    onCancelled: (cancelledCampsite: Campsite?) -> Unit = {},
    onBack: () -> Boolean
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
            onBack = { onBack() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Lavender tab bar with an icon on each tab, like Figma
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null
                    )
                },
                text = { Text("Active") }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null
                    )
                },
                text = { Text("Past") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Body, depending on the tab
        when (selectedTabIndex) {
            0 -> ActiveEntrantReservations(activeReservations, campsiteDB, onCancelled)
            1 -> PastEntrantReservations()
        }
    }
}

/**
 * Displays the entrant's active reservations in one rounded card.
 * Each reservation can be cancelled after a confirmation dialog.
 *
 * @param activeReservations The entrant's active reservations.
 * @param campsiteDB Database used to look up each reservation's campsite.
 * @param onCancelled Called after a reservation is cancelled, with the cancelled campsite.
 */
@Composable
fun ActiveEntrantReservations(
    activeReservations: List<Reservation>,
    campsiteDB: CampsiteDB,
    onCancelled: (cancelledCampsite: Campsite?) -> Unit
) {
    val reservationDB = remember { ReservationDB() }
    // Android ID is the user's ID (same as ProfileScreen)
    val context = LocalContext.current
    val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    var reservations by remember { mutableStateOf<List<Reservation>>(emptyList()) }
    var isLoaded by remember { mutableStateOf(false) }   // Whether the list has finished loading
    // Campsites we have looked up so far, by campsite ID
    var campsites by remember { mutableStateOf<Map<String, Campsite>>(emptyMap()) }
    // The reservation the entrant picked to cancel (null = none picked)
    var selectedReservation by remember { mutableStateOf<Reservation?>(null) }

    // Load this entrant's active reservations, leaving out cancelled ones
    LaunchedEffect(Unit) {
        reservationDB.getActiveReservationsByEntrant(androidId) { loaded ->
            reservations = loaded.filter { !it.isCancelled }
            isLoaded = true
        }
    }

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

    // Only say "none" after loading, so it doesn't flash on screen first
    if (isLoaded && reservations.isEmpty()) {
        EmptyReservationsMessage(
            text = "You have no active reservations."
        )
    } else if (reservations.isNotEmpty()) {
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

                    ReservationRow(
                        reservation = reservation,
                        campsiteName = campsites[reservation.campsiteID]?.name,
                        onCancel = { selectedReservation = reservation }
                    )
                }
            }
        }
    }

    // Ask the entrant to confirm before cancelling
    selectedReservation?.let { toCancel ->
        AlertDialog(
            onDismissRequest = { selectedReservation = null },
            title = { Text("Cancel reservation?") },
            text = { Text("Do you want to cancel this reservation?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Mark it cancelled in the database and remove it from the list
                        toCancel.isCancelled = true
                        reservationDB.updateReservation(toCancel)
                        reservations = reservations - toCancel
                        selectedReservation = null

                        // Pass the campsite on, so the result screen can show it
                        onCancelled(campsites[toCancel.campsiteID])
                    }
                ) {
                    Text("Yes, cancel")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedReservation = null }) {
                    Text("Keep it")
                }
            }
        )
    }
}

/**
 * Displays the entrant's past reservations.
 *
 * Loading real past reservations is its own user story (US 01.06.02),
 * so this tab only shows a message for now.
 */
@Composable
fun PastEntrantReservations() {
    EmptyReservationsMessage(
        text = "Past reservations will appear here."
    )
}

/**
 * One reservation row: campsite name and dates on the left, Cancel on the right.
 *
 * @param reservation Reservation shown in this row.
 * @param campsiteName Name of the reservation's campsite, or null while it is loading.
 * @param onCancel Called when the entrant presses Cancel.
 */
@Composable
private fun ReservationRow(
    reservation: Reservation,
    campsiteName: String?,
    onCancel: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 8.dp,
                top = 10.dp,
                bottom = 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
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

        TextButton(
            onClick = onCancel,
            colors = ButtonDefaults.textButtonColors(
                contentColor = DeclineRed
            )
        ) {
            Text("Cancel")
        }
    }
}

/**
 * Grey message shown in the middle of a tab that has no reservations.
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
fun ShowEntrantViewReservationsScreenPreview() {
    MagpiepineconeTheme {
        EntrantViewReservationsScreen(
            activeReservations = listOf(),
            campsiteDB = CampsiteDB(),
            onBack = { true }
        )
    }
}