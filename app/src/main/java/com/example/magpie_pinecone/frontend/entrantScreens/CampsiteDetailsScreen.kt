package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.magpie_pinecone.R

/**
 * Display's Campsite Details
 * TO-DO
 * Link to actual campsite data
 */

@Composable
fun CampsiteDetailsScreen(
    onBack: () -> Boolean
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
                onClick = { onBack() }
            ) {
                Text("<--")
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = "Campsite",
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center
                )

                // Campsite Details
                Column(
                    modifier = Modifier.fillMaxSize().padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Max Cap.
                    Row() {
                        Text(
                            text = "Max Capacity: 5",
                            fontSize = 16.sp
                        )
                    }

                    // Price and Rating
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$38",
                            fontSize = 16.sp
                        )

                        Text(
                            text = "Rating: 5.0",
                            fontSize = 16.sp
                        )
                    }

                    // Images
                    LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                    ) {
                        item {
                            Image(
                                painter = painterResource(R.drawable.placeholdercampsiteimage),
                                contentDescription = "Campsite Image"
                            )
                        }
                    }

                    // Details.
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
                            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc sit amet tellus bibendum, tristique orci sit amet, ornare neque. Cras mauris nisi, maximus id arcu in, dapibus rutrum urna. Nulla eget felis vel odio semper dapibus non vitae urna. Mauris quis commodo tellus. Ut quis est vulputate massa efficitur laoreet. Quisque ac maximus neque, quis consequat sapien. In non sagittis odio, sit amet ullamcorper dolor.",
                            fontSize = 16.sp
                        )
                    }

                    // Tags
                    Column() {
                        Text(
                            text = "Tags",
                            fontSize = 16.sp
                        )

                        HorizontalDivider()

                        LazyRow(
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            item {
                                Text(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    text = "Tag1",
                                    fontSize = 16.sp
                                )

                                Text(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    text = "Tag2",
                                    fontSize = 16.sp
                                )

                                Text(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    text = "Tag3",
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    // Location
                    Column(modifier = Modifier.padding(vertical = 10.dp)) {
                        Text(
                            text = "Location",
                            fontSize = 16.sp
                        )

                        HorizontalDivider()
                    }

                    // Rate Campsite.
                    Row(modifier = Modifier.padding(vertical = 10.dp)) {
                        Text(
                            text = "Rate this campsite: {rating stuff here}",
                            fontSize = 16.sp
                        )
                    }
                }
            }

            ExtendedFloatingActionButton(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 70.dp),
                onClick = {  }
            ) {
                Text("Enter Lottery")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowCampsiteDetailsScreenPreview() {
    CampsiteDetailsScreen({false})
}