package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.frontend.sharedComposables.ImageCarousel
import com.example.magpie_pinecone.ui.components.CampsiteImage
import com.example.magpie_pinecone.ui.components.PillButton
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme

/*
 * CampsiteResultScreen.kt contains all relevant functions for displaying the result of reserving/cancelling a campsite reservation.
 *
 * Composable Functions:
 * CampsiteResultScreen displays the result of reserving/cancelling a campsite, with an option to download a csv copy.
 * ResultPhoto displays the campsite's photos, or a placeholder if it has none.
 * ResultDetail displays one label and its value, such as "Name" and the campsite's name.
 * ShowCampsiteResultScreenPreview displays a preview of CampsiteResultScreen for reference.
 */

/**
 * Displays the result of reserving or cancelling a campsite.
 *
 * Shows the title and message passed in, followed by the campsite's
 * details. If no campsite is given, only the title and message are shown.
 *
 * @param onBack Returns to the previous screen.
 * @param title Title of the screen, e.g. "Campsite Reserved" or "Campsite Cancelled".
 * @param message Message telling the entrant what happened.
 * @param campsite The campsite that was reserved or cancelled, if known.
 */
@Composable
fun CampsiteResultScreen(
    onBack: () -> Boolean,
    title: String = "Campsite Reserved/Cancelled",
    message: String = "",
    campsite: Campsite? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        // Everything above the Download CSV button scrolls
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PineconeTopBar(
                title = title,
                onBack = { onBack() }
            )

            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Only show campsite details when we know which campsite it was
            if (campsite != null) {
                Spacer(modifier = Modifier.height(16.dp))

                ResultPhoto(
                    campsite = campsite
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Details are kept narrow to match the Figma layout
                Column(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ResultDetail(
                        label = "Name",
                        value = campsite.name.ifBlank { "Unnamed campsite" }
                    )
                    ResultDetail(
                        label = "Location",
                        value = campsite.location.ifBlank { "No location provided" }
                    )
                    ResultDetail(
                        label = "Description",
                        value = campsite.description.ifBlank { "No description provided" }
                    )
                    ResultDetail(
                        label = "Max Occupancy",
                        value = campsite.maxOccupancy.toString()
                    )

                    // Map location is optional, so only show it when it is set
                    val mapLocation = campsite.mapLocation
                    if (mapLocation != null) {
                        ResultDetail(
                            label = "Map Location",
                            value = "%.4f, %.4f".format(mapLocation.latitude, mapLocation.longitude)
                        )
                    }

                    ResultDetail(
                        label = "Tags",
                        value = campsite.tags.joinToString(", ").ifBlank { "None" }
                    )
                }
            }
        }

        // CSV download is its own user story, so the button does nothing yet
        PillButton(
            text = "Download CSV",
            onClick = { },
            icon = Icons.Filled.Download,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 12.dp)
                .height(52.dp)
        )
    }
}

/**
 * Shows the campsite's photos in the shared image carousel.
 * Falls back to the placeholder picture when the campsite has no photos yet.
 *
 * @param campsite Campsite whose photos are displayed.
 */
@Composable
private fun ResultPhoto(
    campsite: Campsite
) {
    if (campsite.images.isEmpty()) {
        CampsiteImage(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(160.dp)
        )
    } else {
        ImageCarousel(
            imgDataList = campsite.images,
            height = 160.dp
        )
    }
}

/**
 * Displays one label and value, like "Name" above "Lakeview Campsite".
 * Same look as the details on the profile screen.
 *
 * @param label Name of the detail.
 * @param value Value of the detail.
 */
@Composable
private fun ResultDetail(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ShowCampsiteResultScreenPreview() {
    MagpiepineconeTheme {
        CampsiteResultScreen(
            onBack = { true },
            title = "Campsite Reserved",
            message = "Your reservation was placed successfully!",
            campsite = Campsite(
                name = "Lakeview Campsite",
                location = "Random Location",
                description = "Random description",
                maxOccupancy = 5,
                tags = listOf("Lake")
            )
        )
    }
}