package com.example.magpie_pinecone.frontend.ownerScreens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.sharedDataClasses.ImageData
import com.example.magpie_pinecone.backend.sharedDataClasses.ImageViewModel
import com.example.magpie_pinecone.frontend.sharedComposables.ImageCarousel
import com.example.magpie_pinecone.ui.components.PillButton
import com.example.magpie_pinecone.ui.components.PineconeTextField
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import com.example.magpie_pinecone.ui.theme.PineconePurple

/*
 * EditCampsiteScreen.kt contains all relevant functions for displaying the screen to register/edit a campsite.
 *
 * Composable Functions:
 * EditCampsiteScreen displays the screen to register/edit a campsite, with fields for each required detail.
 * MapLocationPlaceholder displays the box where the campsite's map location will go.
 * ShowEditCampsiteScreenPreview displays a preview of EditCampsiteScreen for reference.
 */

/**
 * Displays the screen where an owner registers a new campsite or edits an existing one.
 *
 * The fields start with the campsite's current values. Pressing the button
 * at the bottom copies the typed values into the campsite and submits it.
 *
 * @param ownerId ID of the owner the campsite belongs to.
 * @param isEdit True when editing an existing campsite, false when registering a new one.
 * @param campsite The campsite being registered or edited.
 * @param onBack Returns to the previous screen.
 * @param onSubmit Called with the filled-in campsite when the owner submits.
 * @param imageViewModel Holds the campsite's images (a parameter so tests can replace it).
 */

@Composable
fun EditCampsiteScreen(
    ownerId: String,
    isEdit: Boolean,
    campsite: Campsite,
    onBack: () -> Unit,
    onSubmit: (Campsite) -> Unit,
    imageViewModel: ImageViewModel = viewModel()  // This is in parameters so it can be modified easily in tests
) {

    var name by remember { mutableStateOf(campsite.name) }
    var location by remember { mutableStateOf(campsite.location) }
    var price by remember { mutableStateOf(if (campsite.price == 0.0) "" else campsite.price.toString()) }
    var description by remember { mutableStateOf(campsite.description) }
    var maxOccupancy by remember { mutableStateOf(if (campsite.maxOccupancy == 0) "" else campsite.maxOccupancy.toString()) }
    var tags by remember { mutableStateOf(campsite.tags.joinToString(", ")) }
    val imageDataList by imageViewModel.images.collectAsStateWithLifecycle()
    //    var mapLocation by remember { mutableStateOf("")}  // TEMPORARY, NEED TO IMPLEMENT MAP

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        // Everything above the button scrolls, so all the fields fit
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PineconeTopBar(
                title = if (isEdit) "Edit Campsite" else "Register Campsite",
                onBack = onBack
            )

                ImageCarousel(
                    imageDataList,
                    onUpload = {
                        imageViewModel.addImagesAndUploadToDB(it)
                    },
                    onDelete = {
                        imageViewModel.deleteImageAndDeleteFromDB(it)
                    },
                    allowModifying = true
                )

            // Campsite details
            PineconeTextField(
                value = name,
                onValueChange = { name = it },
                label = "Name"
            )
            PineconeTextField(
                value = location,
                onValueChange = { location = it },
                label = "Location"
            )
            PineconeTextField(
                value = price,
                onValueChange = { price = it },
                label = "Price ($)",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )
            PineconeTextField(
                value = description,
                onValueChange = { description = it },
                label = "Description",
                singleLine = false   // Descriptions can be a few lines long
            )
            PineconeTextField(
                value = maxOccupancy,
                onValueChange = { maxOccupancy = it },
                label = "Max Occupancy",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            PineconeTextField(
                value = tags,
                onValueChange = { tags = it },
                label = "Tags"
            )

            MapLocationPlaceholder()
        }

        // Submit button pinned at the bottom of the screen
        PillButton(
            text = if (isEdit) "Save Campsite" else "Register Campsite",
            onClick = {
                campsite.ownerId = ownerId
                campsite.name = name
                campsite.location = location
                campsite.price = price.toDoubleOrNull() ?: 0.0
                campsite.description = description
                campsite.maxOccupancy = maxOccupancy.toIntOrNull() ?: 0
                // Tags are typed as "River, Lake" and saved as a list
                campsite.tags = tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }

                onSubmit(campsite)
            },
            icon = Icons.Filled.Check,
            containerColor = PineconePurple,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(52.dp)
        )
    }
}

/**
 * Outlined box where the campsite's map location will go.
 *
 * Picking a map location needs a maps library, so this only marks
 * the spot for now, like the "Map Location" box in Figma.
 */
@Composable
private fun MapLocationPlaceholder() {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Map,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Map location",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Coming soon",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ShowEditCampsiteScreenPreview() {
    MagpiepineconeTheme {
        EditCampsiteScreen(
            ownerId = "",
            isEdit = false,
            campsite = Campsite(),
            onBack = { },
            onSubmit = {}
        )
    }
}