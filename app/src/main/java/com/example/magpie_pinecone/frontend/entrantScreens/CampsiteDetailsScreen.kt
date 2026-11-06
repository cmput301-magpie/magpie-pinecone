package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magpie_pinecone.R
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite

/**
 * Displays campsite details.
 *
 * @param campsite The campsite whose information is being displayed.
 * @param onBack Returns to the previous screen.
 */
@Composable
fun CampsiteDetailsScreen(
    campsite: Campsite,
    onBack: () -> Unit,
    onSubmit: () -> Unit = {},
    isWinner: Boolean = false,
    onReserve: () -> Unit = {}
) {
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
                onClick = onBack
            ) {
                Text("<--")
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campsite name
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = campsite.name,
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center
                )

                // Campsite details
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Maximum occupancy
                    Row {
                        Text(
                            text = "Max Capacity: ${campsite.maxOccupancy}",
                            fontSize = 16.sp
                        )
                    }

                    // Price and rating
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "\$${campsite.price}",
                            fontSize = 16.sp
                        )

                        Text(
                            text = "Rating: ${campsite.rating}",
                            fontSize = 16.sp
                        )
                    }

                    // Campsite image placeholder
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    ) {
                        item {
                            Image(
                                painter = painterResource(
                                    R.drawable.placeholdercampsiteimage
                                ),
                                contentDescription = "Campsite Image"
                            )
                        }
                    }

                    // Description
                    Column(
                        modifier = Modifier.padding(vertical = 10.dp)
                    ) {
                        Text(
                            text = "Details",
                            fontSize = 16.sp
                        )

                        HorizontalDivider()

                        Text(
                            modifier = Modifier.padding(vertical = 10.dp),
                            text = campsite.description,
                            fontSize = 16.sp
                        )
                    }

                    // Tags
                    Column {
                        Text(
                            text = "Tags",
                            fontSize = 16.sp
                        )

                        HorizontalDivider()

                        LazyRow(
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            items(campsite.tags) { tag ->
                                Text(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    text = tag,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    // Location
                    Column(
                        modifier = Modifier.padding(vertical = 10.dp)
                    ) {
                        Text(
                            text = "Location",
                            fontSize = 16.sp
                        )

                        HorizontalDivider()

                        Text(
                            modifier = Modifier.padding(vertical = 10.dp),
                            text = campsite.location,
                            fontSize = 16.sp
                        )
                    }

                    // Rating functionality will be implemented separately
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp)
                    ) {
                        Text(
                            text = "Rate this campsite: {rating stuff here}",
                            fontSize = 16.sp
                        )
                    }
                }
            }

            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 70.dp),
                    onClick = onSubmit

            ) {
                Text("Enter Lottery")
            }
            // Lottery functionality will be connected later
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 70.dp),
                onClick = { }
            ) {
                Text("Enter Lottery")
            }
            // Lottery functionality will be connected later
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 70.dp),
                    onClick = { }
                ) {
                    Text("Enter Lottery")
            }

            // Only lottery winners can go on to reserve dates
            if (isWinner) {
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 130.dp),
                    onClick = onReserve
                ) {
                    Text("Reserve")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowCampsiteDetailsScreenPreview() {
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
        onSubmit = { }
    )
}