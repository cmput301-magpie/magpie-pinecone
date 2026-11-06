package com.example.magpie_pinecone.frontend.ownerScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite

/**
 * TO-DO
 * Implement Image Picking
 * Implement Map Location
 * Link Textfields to Campsite Data
 * Implement Create/Save Campsite
 * Implement switching between Creating and Saving Campsite
 */

@Composable
fun EditCampsiteScreen(
    isEdit: Boolean,
    campsite: Campsite,
    onBack: () -> Boolean,
    onSubmit: (Campsite) -> Unit,
) {

    var name by remember { mutableStateOf(campsite.name) }
    var location by remember { mutableStateOf(campsite.location) }
    var price by remember { mutableStateOf(if (campsite.price == 0.0) "" else campsite.price.toString()) }
    var description by remember { mutableStateOf(campsite.description) }
    var maxOccupancy by remember { mutableStateOf(if (campsite.maxOccupancy == 0) "" else campsite.maxOccupancy.toString()) }
    var tags by remember { mutableStateOf(campsite.tags.joinToString(", ")) }
//    var mapLocation by remember { mutableStateOf("")}  // TEMPORARY, NEED TO IMPLEMENT MAP

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
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
                if (isEdit) {
                    Text(
                        modifier = Modifier.padding(20.dp),
                        text = "Edit Campsite",
                        fontSize = 36.sp
                    )
                } else {
                    Text(
                        modifier = Modifier.padding(20.dp),
                        text = "Create Campsite",
                        fontSize = 36.sp
                    )
                }

                // Image Picker Placeholder
                Text("Image")

                // Campsite Details Input Fields - Need to implement actual campsite data to be modified
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") }
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price ($)") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )

                OutlinedTextField(
                    value = maxOccupancy,
                    onValueChange = { maxOccupancy = it },
                    label = { Text("Max Occupancy") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags") }
                )

                // Placeholder Map Location
                Text("Map Location")
            }

            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(100.dp),
                onClick = {
                    campsite.name = name
                    campsite.location = location
                    campsite.price = price.toDoubleOrNull() ?: 0.0
                    campsite.description = description
                    campsite.maxOccupancy = maxOccupancy.toIntOrNull() ?: 0
                    // TEMPORARY, adding tags is not finished.
                    campsite.tags = tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }

                    onSubmit(campsite)
                }
            ) {
                if (isEdit) {
                    Text("Save Campsite")
                } else {
                    Text("Register Campsite")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowEditCampsiteScreenPreview() {
    EditCampsiteScreen(isEdit = false, campsite = Campsite(), onBack = { false }, onSubmit = {},)
}