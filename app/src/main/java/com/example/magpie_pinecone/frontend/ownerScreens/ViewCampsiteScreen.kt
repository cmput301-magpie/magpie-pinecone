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
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite

/**
 * TO-DO
 * Link actual campsite data to text, implement image and map location
 * Link to edit campsite
 */

@Composable
fun ViewCampsiteScreen(
    campsite: Campsite,
    onBack: () -> Boolean,
    onEdit: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier.align(Alignment.TopStart),
                onClick = { onBack() }
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
                    text = campsite.name,
                    fontSize = 30.sp
                )

                Text(
                    text = campsite.location,
                    fontSize = 30.sp
                )

                Text(
                    text = $$"$ $${campsite.price}",
                    fontSize = 30.sp
                )

                Text(
                    text = campsite.description,
                    fontSize = 30.sp
                )

                Text(
                    text = "Max Occupancy: ${campsite.maxOccupancy}",
                    fontSize = 30.sp
                )

                Text(
                    text = "Tags: ${campsite.tags.joinToString(", ")}",  // subject to change
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
                onClick = onEdit
            ) {
                Text("Edit Campsite")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowViewCampsiteScreenPreview() {
    ViewCampsiteScreen(campsite = Campsite(name = "Test Site"), onBack = { false }, onEdit = {})
}
