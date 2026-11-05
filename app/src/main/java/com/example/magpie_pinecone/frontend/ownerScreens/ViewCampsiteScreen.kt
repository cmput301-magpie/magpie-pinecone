package com.example.magpie_pinecone.frontend.ownerScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * TO-DO
 * Link actual campsite data to text, implement image and map location
 * Link to edit campsite
 */

@Composable
fun ViewCampsiteScreen(
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
                    text = "View Campsite",
                    fontSize = 36.sp
                )

                // Image Picker Placeholder
                Text(
                    text = "Image",
                    fontSize = 30.sp
                )

                // Campsite Details
                Text(
                    text = "Name",
                    fontSize = 30.sp
                )

                Text(
                    text = "Location",
                    fontSize = 30.sp
                )

                Text(
                    text = "Description",
                    fontSize = 30.sp
                )

                Text(
                    text = "Max Occupancy",
                    fontSize = 30.sp
                )

                Text(
                    text = "Tags",
                    fontSize = 30.sp
                )

                // Placeholder Map Location
                Text(
                    text = "Map Location",
                    fontSize = 30.sp
                )
            }

            ExtendedFloatingActionButton(
                modifier = Modifier.align(Alignment.BottomCenter).padding(100.dp),
                onClick = { TODO("Edit Campsite") }
            ) {
                Text("Edit Campsite")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowViewCampsiteScreenPreview() {
    ViewCampsiteScreen(false)
}
