package com.example.magpie_pinecone.frontend.ownerScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Tab
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magpie_pinecone.R
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OwnerHomeScreen(
    campsites: List<Campsite>,
    lotteries: List<Lottery>,
    onEditLottery: (Lottery) -> Unit,
    onCreateLottery: () -> Unit,
    onViewWaitlist: () -> Unit,
    // onQRCode: () -> Unit, implement after halfway point
    // onNotifications: () -> Unit, implement after halfway point
    onEditCampsite: (Campsite) -> Unit,
    onCreateCampsite: () -> Unit,
    onPreviewCampsite: (Campsite) -> Unit,
    onDatesBooked: () -> Unit,
    onViewReservations: () -> Unit
) {
    // Tab Navigation
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(1) }
    val tabs = listOf("My Lotteries", "My Campsites")

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title, may switch owner out with user's actual name
        Text(
            modifier = Modifier.padding(20.dp),
            text = "Welcome Back, Owner!",
            fontSize = 36.sp
        )

        // Tabs
        PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
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
                onViewReservations
            )
        }
    }
}

// My Lotteries Body
@Composable
fun MyLotteries(
    lotteries: List<Lottery>,
    onEditLottery: (Lottery) -> Unit,
    onCreateLottery: () -> Unit,
    onViewWaitlist: () -> Unit,
) {

    Box(modifier = Modifier.fillMaxHeight()) {
        LazyColumn(modifier = Modifier.padding(vertical = 10.dp)) {
            items(lotteries) { lottery ->
                LotteryCard(
                    lottery = lottery,
                    menuItems = listOf(
                        "Edit" to { onEditLottery(lottery) },
                        "View waitlist" to onViewWaitlist
                    )
                )
            }
        }
        ExtendedFloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = { onCreateLottery() }
        ) { Text("+ Lottery") }
    }

    if (lotteries.isEmpty()) {
        Text(
            modifier = Modifier.padding(20.dp),
            text = "You have no lotteries yet.",
            fontSize = 16.sp
        )
        return
    }
}

@Composable
fun LotteryCard(
    lottery: Lottery,
    menuItems: List<Pair<String, () -> Unit>>
) {
    var expanded by remember { mutableStateOf(false) }

    // A lottery has no name of its own, so title it using its campsites
    val title = lottery.campsites
        .map { it.name.ifBlank { "Unnamed campsite" } }
        .joinToString(", ")
        .ifBlank { "Unnamed lottery" }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(10.dp)
    ) {
        Box {
            Row(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = title,
                        fontSize = 32.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(0.6f)
                    )
                    Text(
                        text = "Registration: ${formatDate(lottery.registrationStartDate)} - ${formatDate(lottery.registrationEndDate)}",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Booking: ${formatDate(lottery.bookingStartDate)} - ${formatDate(lottery.bookingEndDate)}",
                        fontSize = 16.sp
                    )
                    Text(text = "Winners: ${lottery.numberOfWinners}", fontSize = 16.sp)
                    Text(text = "Waitlist capacity: ${lottery.waitlistCapacity}", fontSize = 16.sp)
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_favorite),
                    contentDescription = "Lottery Image"
                )
            }

            Column(modifier = Modifier.align(Alignment.TopEnd)) {
                SmallFloatingActionButton(onClick = { expanded = !expanded }) {
                    Text("...")
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
    }
}

// Formats a date as MM/dd/yyyy, or "TBD" if the date has not been set
private fun formatDate(date: Date?): String =
    date?.let { SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(it) } ?: "TBD"

// My Campsites Body
@Composable
fun MyCampsites(
    campsites: List<Campsite>,
    onEditCampsite: (Campsite) -> Unit,
    onCreateCampsite: () -> Unit,
    onPreviewCampsite: (Campsite) -> Unit,
    onDatesBooked: () -> Unit,
    onViewReservations: () -> Unit
) {


    Box(modifier = Modifier.fillMaxSize().padding(5.dp)) {
        LazyColumn(modifier = Modifier.padding(vertical = 10.dp)) {
            // For each campsite, display a card with the details
            items(campsites) { campsite ->
                CampsiteCard(
                    campsite = campsite,
                    menuItems = listOf(
                        "Edit" to { onEditCampsite(campsite) },
                        "Preview" to { onPreviewCampsite(campsite) },
                        "Dates Booked" to onDatesBooked
                    )
                )
            }
        }

        // Floating buttons to add campsite and view campsite reservations
        Column(
            modifier = Modifier.align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.End
        ) {
            ExtendedFloatingActionButton(
                onClick = { onCreateCampsite() }
            ) {
                Text("+ Campsite")
            }
            ExtendedFloatingActionButton(
                modifier = Modifier.padding(top = 10.dp),
                onClick = { onViewReservations() }
            ) {
                Text("Campsite Resv.")
            }
        }
    }

    if (campsites.isEmpty()) {
        Text(
            modifier = Modifier.padding(20.dp),
            text = "You have no campsites yet.",
            fontSize = 16.sp
        )
        return
    }
}

@Composable
fun CampsiteCard(
    campsite: Campsite,
    menuItems: List<Pair<String, () -> Unit>>
) {
    var expanded by remember { mutableStateOf(false) }  // Start dropdown menu collapsed

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(10.dp)
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = campsite.name.ifBlank { "Unnamed campsite" }, fontSize = 32.sp)
                    Text(text = "Rating: ${campsite.rating}", fontSize = 16.sp)
                    Text(text = "Max Occupancy: ${campsite.maxOccupancy}", fontSize = 16.sp)
                    Text(text = $$"$ $${campsite.price}", fontSize = 16.sp)
                    Text(text = campsite.location, fontSize = 16.sp)
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_favorite),
                    contentDescription = "Campsite Image"
                )
            }

            Column(modifier = Modifier.align(Alignment.TopEnd)) {
                SmallFloatingActionButton(onClick = { expanded = !expanded }) {
                    Text("...")
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
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowOwnerHomeScreenPreview() {
    OwnerHomeScreen(
        lotteries = emptyList(),
        campsites = emptyList(),
        onEditLottery = {},
        onCreateLottery = {},
        onViewWaitlist = {},
        onEditCampsite = {},
        onCreateCampsite = {},
        onPreviewCampsite = {},
        onDatesBooked = {},
        onViewReservations = {}
    )
}