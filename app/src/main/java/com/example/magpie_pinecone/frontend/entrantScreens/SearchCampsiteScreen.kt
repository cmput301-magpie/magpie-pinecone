package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.campsiteClasses.CampsiteDB
import androidx.compose.material3.Button
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.foundation.clickable
/**
 * Displays a list of campsites that entrants can search through.
 *
 * @param navController Controls navigation between entrant screens.
 */

@Composable
fun SearchCampsiteScreen(
    navController: NavController,
    onCampsiteClick: (Campsite) -> Unit
) {

    val campsiteDB = remember { CampsiteDB() } // Stores the campsites fetched from the campsite database

    var campsites by remember { mutableStateOf<List<Campsite>>(emptyList()) }

    var searchText by remember { mutableStateOf("") }     // Stores the text entered into the campsite search bar
    var locationText by remember { mutableStateOf("") }// Stores the location filter
    var dateText by remember { mutableStateOf("") }// Stores the date filter
    var guestText by remember { mutableStateOf("") }// Stores the guest number filter

    // Fetch all campsites when the screen is first displayed

    LaunchedEffect(Unit) {
        campsiteDB.searchCampsites(
            name = "",
            location = "",
            date = null,
            guests = null
        ) { campsiteList ->
            campsites = campsiteList
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Search Campsites",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search by campsite name") },
            modifier = Modifier.fillMaxWidth()
        )
        // Campsite search filters
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = locationText,
            onValueChange = { locationText = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = dateText,
            onValueChange = { dateText = it },
            label = { Text("Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = guestText,
            onValueChange = { guestText = it },
            label = { Text("Number of guests") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val guests = guestText.toIntOrNull()

                val date = if (dateText.isBlank()) {
                    null
                } else {
                    try {
                        SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).parse(dateText)
                    } catch (exception: Exception) {
                        null
                    }
                }

                // Apply the entered filters through CampsiteDB
                campsiteDB.searchCampsites(
                    name = searchText,
                    location = locationText,
                    date = date,
                    guests = guests
                ) { results ->
                    campsites = results
                }
            }
        ) {
            Text("Apply Filters")
        }

        Spacer(modifier = Modifier.height(16.dp))


// Displays campsites returned by CampsiteDB
        LazyColumn {
            items(campsites) { campsite ->

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onCampsiteClick(campsite)
                        }
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = campsite.name,
                        fontSize = 20.sp
                    )

                    Text("Location: ${campsite.location}")
                    Text("Max Occupancy: ${campsite.maxOccupancy}")
                    Text("Rating: ${campsite.rating}")
                }
            }
        }
    }
}