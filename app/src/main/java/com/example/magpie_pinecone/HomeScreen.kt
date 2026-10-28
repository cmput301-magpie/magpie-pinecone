package com.example.magpie_pinecone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pinecone Home",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("newUser")
            },
            modifier = Modifier.testTag("newUserButton") // for testing
        ) {
            Text("New User")
        }

        //        Navigate to driverscreen

        Button(
            onClick = {
                navController.navigate("viewCampsite")
            },
            modifier = Modifier.testTag("viewCampsiteButton") // for testing
        ) {
            Text("View Campsites")

        }

        Button(
            onClick = {
                navController.navigate("searchCampsite")
            },
            modifier = Modifier.testTag("searchCampsiteButton") // for testing
        ) {
            Text("Search Campsites")

        }
    }
}