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
import androidx.compose.material3.OutlinedTextField

/**
 * Displays a list of campsites that entrants can search through.
 *
 * @param navController Controls navigation between entrant screens.
 */

@Composable
fun SearchCampsiteScreen(navController: NavController) {

    val campsiteDB = remember { CampsiteDB() } // Stores the campsites fetched from the campsite database

    var campsites by remember { mutableStateOf<List<Campsite>>(emptyList()) }

    var searchText by remember { mutableStateOf("") }     // Stores the text entered into the campsite search bar


    LaunchedEffect(Unit) // Fetch campsites when the screen is first displayed
    {
        campsiteDB.getAllCampsites { campsiteList ->
            campsites = campsiteList
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
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

        Spacer(modifier = Modifier.height(16.dp))

        // Filters campsites by their name using the entered search text
        val filteredCampsites = campsites.filter { campsite ->
            campsite.name.contains(searchText, ignoreCase = true)
        }

        LazyColumn {
            items(filteredCampsites) { campsite ->

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
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