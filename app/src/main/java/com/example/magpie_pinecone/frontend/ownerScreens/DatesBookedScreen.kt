package com.example.magpie_pinecone.frontend.ownerScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * TO-DO
 * Link actual campsite dates to text
 */

@Composable
fun DatesBookedScreen(
    onBack: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
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
                    text = "Dates Booked",
                    fontSize = 36.sp
                )

                Text(
                    modifier = Modifier.padding(20.dp),
                    text = "Jun 17, 2026",
                    fontSize = 30.sp
                )

                Text(
                    modifier = Modifier.padding(20.dp),
                    text = "to",
                    fontSize = 30.sp
                )

                Text(
                    modifier = Modifier.padding(20.dp),
                    text = "Jun 24, 2026",
                    fontSize = 30.sp
                )
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowDatesBookedScreenPreview() {
    DatesBookedScreen(false)
}
