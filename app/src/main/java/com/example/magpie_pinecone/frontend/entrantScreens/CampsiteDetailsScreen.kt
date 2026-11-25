package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.frontend.sharedComposables.ImageCarousel
import com.example.magpie_pinecone.ui.components.CampsiteImage
import com.example.magpie_pinecone.ui.components.PillButton
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.example.magpie_pinecone.ui.components.StarRating
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import com.example.magpie_pinecone.ui.theme.PineconePurple
import com.example.magpie_pinecone.ui.theme.ReserveGreen

/*
 * CampsiteDetailsScreen.kt contains all relevant functions for displaying the details of a campsite.
 *
 * Composable Functions:
 * CampsiteDetailsScreen displays details of a campsite, with a button to enter a lottery for the campsite.
 * CampsiteDetailsLayout displays the details page shared by entrants and owners, with any buttons pinned at the bottom.
 * CampsiteSummaryRow displays the campsite's price, maximum occupancy and rating.
 * CampsitePhotos displays the campsite's images, or a placeholder if it has none.
 * SectionLabel displays a small gray heading with a divider line under it.
 * ExpandableDescription displays the description, with "read more..." when it is too long.
 * TagChip displays one campsite tag inside an outlined box.
 * RateCampsiteRow displays the "Rate this campsite" row of stars.
 * ShowCampsiteDetailsScreenPreview displays a preview of CampsiteDetailsScreen for reference.
 */

/**
 * Displays campsite details to an entrant, with the lottery buttons at the bottom.
 *
 * @param campsite The campsite whose information is being displayed.
 * @param onBack Returns to the previous screen.
 * @param onSubmit Called when the entrant enters the campsite's lottery.
 * @param isWinner Whether the entrant won this campsite's lottery.
 * @param onReserve Called when a lottery winner chooses to reserve.
 */
@Composable
fun CampsiteDetailsScreen(
    campsite: Campsite,
    onBack: () -> Unit,
    onSubmit: () -> Unit = {},
    isWinner: Boolean = false,
    onReserve: () -> Unit = {}
) {
    CampsiteDetailsLayout(
        campsite = campsite,
        onBack = onBack
    ) {
        // Only lottery winners can go on to reserve dates
        if (isWinner) {
            PillButton(
                text = "Reserve",
                onClick = onReserve,
                containerColor = ReserveGreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            )
        }

        PillButton(
            text = "Enter lottery",
            onClick = onSubmit,
            containerColor = PineconePurple,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        )
    }
}

/**
 * The campsite details page shared by entrants and owners.
 *
 * The details scroll, while the buttons passed in stay pinned at the
 * bottom. Entrants get the lottery buttons; owners previewing their own
 * campsite get an "Edit Campsite" button instead.
 *
 * @param campsite The campsite whose information is being displayed.
 * @param onBack Returns to the previous screen.
 * @param bottomButtons The buttons pinned at the bottom of the screen.
 */
@Composable
fun CampsiteDetailsLayout(
    campsite: Campsite,
    onBack: () -> Unit,
    bottomButtons: @Composable () -> Unit
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
        // Everything above the buttons scrolls, so long descriptions still fit
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            PineconeTopBar(
                title = campsite.name.ifBlank { "Unnamed campsite" },
                onBack = onBack
            )

            CampsiteSummaryRow(
                campsite = campsite
            )

            Spacer(modifier = Modifier.height(12.dp))

            CampsitePhotos(
                campsite = campsite
            )

            SectionLabel(
                text = "Details"
            )
            ExpandableDescription(
                description = campsite.description
            )

            // Only show the tags section when the campsite has tags
            if (campsite.tags.isNotEmpty()) {
                SectionLabel(
                    text = "Tags"
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(campsite.tags) { tag ->
                        TagChip(
                            tag = tag
                        )
                    }
                }
            }

            SectionLabel(
                text = "Location"
            )
            Text(
                text = campsite.location.ifBlank { "No location provided." },
                style = MaterialTheme.typography.bodyMedium
            )

            RateCampsiteRow()
        }

        // Buttons pinned at the bottom of the screen
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            bottomButtons()
        }
    }
}

/**
 * Shows the price on the left, and the maximum occupancy and rating on the right.
 *
 * @param campsite Campsite whose summary is displayed.
 */
@Composable
private fun CampsiteSummaryRow(
    campsite: Campsite
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = "\$${formatPrice(campsite.price)}",
            style = MaterialTheme.typography.titleLarge
        )

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = campsite.maxOccupancy.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.People,
                    contentDescription = "Maximum occupancy",
                    modifier = Modifier.size(20.dp)
                )
            }

            StarRating(
                rating = campsite.rating
            )
        }
    }
}

/**
 * Shows the campsite's photos in the shared image carousel.
 * Falls back to the placeholder picture when the campsite has no photos yet.
 *
 * @param campsite Campsite whose photos are displayed.
 */
@Composable
private fun CampsitePhotos(
    campsite: Campsite
) {
    if (campsite.images.isEmpty()) {
        CampsiteImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )
    } else {
        ImageCarousel(
            imgDataList = campsite.images,
            height = 180.dp
        )
    }
}

/**
 * Small grey heading with a divider line under it, like the Figma section titles.
 *
 * @param text Name of the section.
 */
@Composable
private fun SectionLabel(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(
            top = 16.dp,
            bottom = 4.dp
        )
    )
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant
    )
    Spacer(modifier = Modifier.height(8.dp))
}

/**
 * Shows the campsite description, cut off after a few lines.
 * A "read more..." button appears only when the text does not fit.
 *
 * @param description Description of the campsite.
 */
@Composable
private fun ExpandableDescription(
    description: String
) {
    var expanded by remember { mutableStateOf(false) }    // Whether the full text is showing
    var isTooLong by remember { mutableStateOf(false) }   // Whether the text was cut off

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = description.ifBlank { "No description provided." },
            style = MaterialTheme.typography.bodyMedium,
            maxLines = if (expanded) Int.MAX_VALUE else 6,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { result ->
                // Only check while collapsed, otherwise the button would disappear after expanding
                if (!expanded) {
                    isTooLong = result.hasVisualOverflow
                }
            }
        )

        if (isTooLong) {
            TextButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = if (expanded) "show less" else "read more..."
                )
            }
        }
    }
}

/**
 * Shows one tag inside an outlined rounded box.
 *
 * @param tag Text of the tag.
 */
@Composable
private fun TagChip(
    tag: String
) {
    Text(
        text = tag,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
                horizontal = 12.dp,
                vertical = 6.dp
            )
    )
}

/**
 * "Rate this campsite" row with five empty stars.
 *
 * Rating is its own user story (US 01.13.01), so the stars are only
 * displayed for now and do not save anything yet.
 */
@Composable
private fun RateCampsiteRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Rate this campsite",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )

        repeat(5) {
            Icon(
                imageVector = Icons.Outlined.StarBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * Formats prices without unnecessary decimal places (38.0 becomes "38").
 * Same rule as the entrant home screen, so prices look the same everywhere.
 */
private fun formatPrice(
    price: Double
): String {
    return if (price % 1.0 == 0.0) {
        price.toInt().toString()
    } else {
        "%.2f".format(price)
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ShowCampsiteDetailsScreenPreview() {
    MagpiepineconeTheme {
        CampsiteDetailsScreen(
            campsite = Campsite(
                name = "Sample Campsite",
                location = "Edmonton",
                maxOccupancy = 5,
                price = 38.0,
                rating = 4.5,
                description = "A sample campsite used for previewing the details screen.",
                tags = listOf("Forest", "Camping", "Family")
            ),
            onBack = { },
            onSubmit = { },
            isWinner = true
        )
    }
}