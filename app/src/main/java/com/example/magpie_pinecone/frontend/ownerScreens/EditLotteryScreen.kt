package com.example.magpie_pinecone.frontend.ownerScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * TO-DO
 * Implement Multiple Campsite Selecting
 * Link Textfields to Lottery Data
 * Implement Date Pickers
 * Implement Start/Update Lottery
 * Implement Swtiching between Starting and Updating Lottery
 */

@Composable
fun EditLotteryScreen(
    onBack: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            Button(
                modifier = Modifier.align(Alignment.TopStart),
                onClick = { onBack }
            ) {
                Text("<--")
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = "Start/Update Lottery",
                    fontSize = 36.sp
                )

                // Campsite Details Input Fields - Need to implement actual campsite data to be modified
                OutlinedTextField(
                    state = rememberTextFieldState(),
                    label = { Text("Num. winners") }
                )

                OutlinedTextField(
                    state = rememberTextFieldState(),
                    label = { Text("Max. waitlist cap.") }
                )

                OutlinedTextField(
                    state = rememberTextFieldState(),
                    label = { Text("Regist") }
                )

                OutlinedTextField(
                    state = rememberTextFieldState(),
                    label = { Text("Max Occupancy") }
                )

                // Date picker placeholders

                OutlinedTextField(
                    state = rememberTextFieldState(),
                    label = { Text("Registration Period") }
                )

                OutlinedTextField(
                    state = rememberTextFieldState(),
                    label = { Text("Booking Period") }
                )
            }

            ExtendedFloatingActionButton(
                modifier = Modifier.align(Alignment.BottomCenter).padding(100.dp),
                onClick = { TODO("Start/Update Lottery") }
            ) {
                Text("Start Lottery")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowEditLotteryScreenPreview() {
    EditLotteryScreen(false)
}