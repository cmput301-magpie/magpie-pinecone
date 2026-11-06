package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.provider.Settings
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.magpie_pinecone.backend.reservationClasses.Reservation
import com.example.magpie_pinecone.backend.reservationClasses.ReservationDB
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Display's Entrant's Reservations (Active and Past)
 * TO-DO
 * Link active and past campsite reservations to iterate over
 * Make actual cancel campsite reservation functionality
 */

@Composable
fun EntrantViewReservationsScreen(onCancelled: () -> Unit = {}) {
    // Tab Navigation
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("Active", "Past")

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            modifier = Modifier.padding(20.dp),
            text = "View Reservations",
            fontSize = 36.sp
        )

        // Tabs
        PrimaryTabRow(
            modifier = Modifier.padding(bottom = 16.dp),
            selectedTabIndex = selectedTabIndex
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index},
                    enabled = true,
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp
                        )
                    }
                )
            }
        }

        // Body, depending on the tab
        when (selectedTabIndex) {
            0 -> ActiveEntrantReservations(onCancelled)
            1 -> PastEntrantReservations()
        }

    }
}

@Composable
fun ActiveEntrantReservations(onCancelled: () -> Unit) {
    val reservationDB = remember { ReservationDB() }
    // Android ID is the user's ID (same as ProfileScreen)
    val context = LocalContext.current
    val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }

    var reservations by remember { mutableStateOf<List<Reservation>>(emptyList()) }
    // The reservation the entrant picked to cancel (null = none picked)
    var selectedReservation by remember { mutableStateOf<Reservation?>(null) }

    // Load this entrant's active reservations, leaving out cancelled ones
    LaunchedEffect(Unit) {
        reservationDB.getActiveReservationsByEntrant(androidId) { loaded ->
            reservations = loaded.filter { !it.isCancelled }
        }
    }

    if (reservations.isEmpty()) {
        Text("No active reservations", fontSize = 20.sp)
    }

    LazyColumn() {
        items(reservations) { reservation ->
            Row(
                modifier = Modifier.fillMaxWidth().border(width = 1.dp, color = Color.Black).padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    // TODO: show the campsite name instead of its ID
                    Text(text = "Campsite ${reservation.campsiteID}", fontSize = 20.sp)
                    Text(
                        text = "${dateFormat.format(reservation.startDate)} - ${dateFormat.format(reservation.endDate)}",
                        fontSize = 16.sp
                    )
                }

                Button(
                    onClick = { selectedReservation = reservation }
                ) {
                    Text("Cancel")
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
                        onCancelled()
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

@Composable
fun PastEntrantReservations() {
    LazyColumn() {
        // Single placeholder campsite, change to iterate over list later
        item {
            Row(
                modifier = Modifier.fillMaxWidth().border(width = 1.dp, color = Color.Black).padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Campsite",
                    fontSize = 30.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowEntrantViewReservationsScreenPreview() {
    EntrantViewReservationsScreen()
}