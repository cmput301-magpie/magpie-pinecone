package com.example.magpie_pinecone.frontend.ownerScreens

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
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.campsiteClasses.CampsiteDB
import com.example.magpie_pinecone.backend.reservationClasses.Reservation

/**
 * TO-DO
 * Link actual reservation data to loop over (Active, Past, Cancelled)
 * Implement view details to actually link to the view campsite screen
 */

@Composable
fun ViewReservationsScreen(
    activeReservations: List<Reservation>,
    campsiteDB: CampsiteDB
) {
    // Tab Navigation
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("Active", "Past", "Cancelled")

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
            0 -> ActiveReservations(activeReservations, campsiteDB)
            1 -> PastReservations()
            2 -> CancelledReservations()
        }
    }
}

@Composable
fun ActiveReservations(
    activeReservations: List<Reservation>,
    campsiteDB: CampsiteDB
) {
    LazyColumn() {
        // Single placeholder campsite, change to iterate over list later
        item {
            activeReservations.forEach { reservation ->
                Row(
                    modifier = Modifier.fillMaxWidth().border(width = 1.dp, color = Color.Black).padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var campsite: Campsite? = Campsite(name = "Test")
                    campsiteDB.readCampsite(reservation.campsiteID) { campsite = it }

                    Text(
                        text = campsite?.name ?: "Campsite",
                        fontSize = 30.sp
                    )

                    Button(
                        onClick = {
                        }
                    ) {
                        Text("View Details")
                    }
                }
            }
        }
    }
}

@Composable
fun PastReservations() {
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

                Button(
                    onClick = {}
                ) {
                    Text("View Details")
                }
            }
        }
    }
}

@Composable
fun CancelledReservations() {
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

                Button(
                    onClick = {}
                ) {
                    Text("View Details")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowViewReservationsScreenPreview() {
    ViewReservationsScreen(listOf(), campsiteDB = CampsiteDB())
}