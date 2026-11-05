package com.example.magpie_pinecone.frontend.ownerScreens

import android.R.attr.text
import android.util.Log.d
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.testing.TestNavHostController
import com.example.magpie_pinecone.R
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import kotlinx.coroutines.selects.select
import kotlin.math.exp

@Composable
fun OwnerHomeScreen(
    onEditLottery: () -> Unit,
    onViewWaitlist: () -> Unit,
    // onQRCode: () -> Unit, implement after halfway point
    onNotifications: () -> Unit,
    onEditCampsite: () -> Unit,
    onPreviewCampsite: () -> Unit,
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
            0 -> MyLotteries(onEditLottery, onViewWaitlist, onNotifications)
            1 -> MyCampsites(onEditCampsite, onPreviewCampsite, onDatesBooked, onViewReservations)
        }
    }
}

// My Lotteries Body
@Composable
fun MyLotteries(
    onEditLottery: () -> Unit,
    onViewWaitlist: () -> Unit,
    // onQRCode: () -> Unit, implement after halfway point
    onNotifications: () -> Unit
) {
    // Dropdown Menu Items
    val menuItems = listOf("Edit", "View waitlist", "QR Code", "Notifications")
    val menuFunctions = listOf(onEditLottery, onViewWaitlist, onNotifications)

    LazyColumn(modifier = Modifier.padding(vertical = 10.dp)) {
        // For now, just a single card as placeholder, will need to change into loop over all campsites
        item {
            Card(
                modifier = Modifier.fillMaxWidth().height(200.dp).padding(10.dp)
            ) {
                Box() {
                    // Dropdown Menu State
                    var expanded by remember { mutableStateOf(false) }

                    Row(
                        modifier = Modifier.fillMaxSize().padding(20.dp),
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
                                text = "Ends 10/19/2026",
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
                            painter = painterResource(id = R.drawable.ic_favorite),
                            contentDescription = "Campsite Image"
                        )
                    }

                    // Dropdown Menu
                    Column(modifier = Modifier.align(Alignment.TopEnd)) {
                        SmallFloatingActionButton(
                            onClick = {
                                expanded = !expanded
                            }
                        ) {
                            Text("...")
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            menuItems.zip(menuFunctions).forEach { (title, function) ->
                                DropdownMenuItem(
                                    text = { Text(
                                        text = title
                                    ) },
                                    onClick = { function }
                                )
                            }
                        }
                    }

                }
            }

            Card(
                modifier = Modifier.fillMaxWidth().height(200.dp).padding(10.dp)
            ) {
                Box() {
                    // Dropdown Menu State
                    var expanded by remember { mutableStateOf(false) }

                    Row(
                        modifier = Modifier.fillMaxSize().padding(20.dp),
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
                                text = "Ends 10/19/2026",
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
                            painter = painterResource(id = R.drawable.ic_favorite),
                            contentDescription = "Campsite Image"
                        )
                    }

                    // Dropdown Menu
                    Column(modifier = Modifier.align(Alignment.TopEnd)) {
                        SmallFloatingActionButton(
                            onClick = {
                                expanded = !expanded
                            }
                        ) {
                            Text("...")
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            menuItems.zip(menuFunctions).forEach { (title, function) ->
                                DropdownMenuItem(
                                    text = { Text(
                                        text = title
                                    ) },
                                    onClick = { function }
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}

// My Campsites Body
@Composable
fun MyCampsites(
    onEditCampsite: () -> Unit,
    onPreviewCampsite: () -> Unit,
    onDatesBooked: () -> Unit,
    onViewReservations: () -> Unit
) {
    // Dropdown Menu Items
    val menuItems = listOf("Edit", "Preview", "Dates Booked")
    val menuFunctions = listOf(onEditCampsite, onPreviewCampsite, onDatesBooked)

    Box(modifier = Modifier.fillMaxHeight()) {
        LazyColumn(modifier = Modifier.padding(vertical = 10.dp)) {
            // For now, just a single card as placeholder, will need to change into loop over all campsites
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().height(200.dp).padding(10.dp)
                ) {
                    Box() {
                        // Dropdown Menu State
                        var expanded by remember { mutableStateOf(true) }

                        Row(
                            modifier = Modifier.fillMaxSize().padding(20.dp),
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
                                    text = "Rating: 5",
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
                                painter = painterResource(id = R.drawable.ic_favorite),
                                contentDescription = "Campsite Image"
                            )
                        }

                        // Dropdown Menu
                        Column(modifier = Modifier.align(Alignment.TopEnd)) {
                            SmallFloatingActionButton(
                                onClick = {
                                    expanded = !expanded
                                }
                            ) {
                                Text("...")
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                menuItems.zip(menuFunctions).forEach { (title, function) ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = title
                                            )
                                        },
                                        onClick = { function }
                                    )
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth().height(200.dp).padding(10.dp)
                ) {
                    Box() {
                        // Dropdown Menu State
                        var expanded by remember { mutableStateOf(false) }

                        Row(
                            modifier = Modifier.fillMaxSize().padding(20.dp),
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
                                    text = "Rating: 5",
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
                                painter = painterResource(id = R.drawable.ic_favorite),
                                contentDescription = "Campsite Image"
                            )
                        }

                        // Dropdown Menu
                        Column(modifier = Modifier.align(Alignment.TopEnd)) {
                            SmallFloatingActionButton(
                                onClick = {
                                    expanded = !expanded
                                }
                            ) {
                                Text("...")
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                menuItems.zip(menuFunctions).forEach { (title, function) ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = title
                                            )
                                        },
                                        onClick = { function }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Create Campsite and View Reservations
        Column(
            modifier = Modifier.align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.End
            ) {
            ExtendedFloatingActionButton(
                onClick = { onEditCampsite }
            ) {
                Text("+ Campsite")
            }

            ExtendedFloatingActionButton(
                modifier = Modifier.padding(top = 10.dp),
                onClick = { onViewReservations }
            ) {
                Text("Campsite Resv.")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowPreview() {
    OwnerHomeScreen({}, {}, {}, {}, {}, {}, {})
}