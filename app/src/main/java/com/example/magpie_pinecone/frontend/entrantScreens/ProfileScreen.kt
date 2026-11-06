package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magpie_pinecone.R
import com.example.magpie_pinecone.frontend.ownerScreens.WaitingListScreen

/**
 * Display's Entrant's Profile
 * TO-DO
 * Implement Delete Profile
 * Implement link to edit profile
 * Implement link to actual entrant data
 */

@Composable
fun ProfileScreen(
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
                    text = "Profile",
                    fontSize = 36.sp
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Picture
                    Image(
                        modifier = Modifier.clip(CircleShape).border(2.dp, Color.Black)
                            .size(150.dp),
                        painter = painterResource(R.drawable.squareimageplaceholder),
                        contentDescription = "Campsite Image"
                    )

                    // Entrant Details
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp, vertical = 20.dp)
                    ) {
                        Text(
                            text = "Name",
                            fontSize = 20.sp
                        )
                        Text(
                            text = "John Doe",
                            fontSize = 20.sp
                        )

                        Text(
                            modifier = Modifier.padding(top = 20.dp),
                            text = "Email",
                            fontSize = 20.sp
                        )
                        Text(
                            modifier = Modifier.padding(bottom = 20.dp),
                            text = "johndoe@gmail.com",
                            fontSize = 20.sp
                        )

                        Text(
                            text = "Phone",
                            fontSize = 20.sp
                        )
                        Text(
                            text = "123-456-7890",
                            fontSize = 20.sp
                        )
                    }

                    // Edit Profile Button
                    Button(
                        onClick = {}
                    ) {
                        Text("Edit Profile")
                    }

                    // Delete Profile Button
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text("Delete Profile")
                    }

                }

            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowProfileScreenPreview() {
    ProfileScreen({ false })
}
