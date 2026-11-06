package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.provider.Settings
import androidx.compose.ui.platform.LocalContext
import com.example.magpie_pinecone.backend.UserClasses.UserDB
import androidx.compose.runtime.LaunchedEffect
import com.example.magpie_pinecone.backend.campsiteClasses.CampsiteDB
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Display's Campsite for Entrant to Reserve
 * TO-DO
 * Link actual reservation/decline functionality
 * Make actual date picker
 */

@Composable
fun CampsiteReserveScreen(
    onBack: () -> Boolean,
    campsiteId: String = ""
) {
    var checked by remember { mutableStateOf(false) }
    val userDB = remember { UserDB() }
    // Android ID is the user's ID in Firestore (same as ProfileScreen)
    val context = LocalContext.current
    val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    val campsiteDB = remember { CampsiteDB() }
    var rules by remember { mutableStateOf("Loading rules...") }
    var availableDates by remember { mutableStateOf<List<Date>>(emptyList()) }
    val dateFormat = remember { SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()) }

    // Load the campsite's rules and policy text
    LaunchedEffect(campsiteId) {
        campsiteDB.readCampsite(campsiteId) { campsite ->
            rules = campsite?.rulesAndPolicy ?: "No rules provided for this campsite."
            availableDates = campsite?.availableDates ?: emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                modifier = Modifier.align(Alignment.TopStart),
                onClick = { onBack() }
            ) {
                Text("<--")
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Title
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = "Campsite",
                    fontSize = 36.sp
                )

                // Date picker placeholder
                // Dates the winner can reserve
                Text("Available dates", fontSize = 20.sp)
                if (availableDates.isEmpty()) {
                    Text("No available dates")
                }
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(availableDates) { date ->
                        Text(
                            text = dateFormat.format(date),
                            modifier = Modifier.padding(8.dp),
                            fontSize = 18.sp
                        )
                    }
                }
                // Campsite rules and policies shown before confirming
                Text(
                    text = rules,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // User agreement to policy and guidelines
                    Row() {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { checked = it }
                        )
                        Text("I have read the campsite rules and cancellation policy")
                    }

                    // Reserve and Decline Buttons
                    Button(
                        enabled = checked,  // Cannot reserve until the rules are confirmed
                        onClick = {
                            // Record that this user confirmed the rules
                            userDB.setRulesAccepted(androidId, true)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green
                        ),
                        shape = RectangleShape
                    ) {
                        Text("Reserve")
                    }

                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        ),
                        shape = RectangleShape
                    ) {
                        Text("Decline")
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowCampsiteReserveScreenPreview() {
    CampsiteReserveScreen({ false })
}