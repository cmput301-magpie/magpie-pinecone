package com.example.magpie_pinecone.frontend.userScreens

import androidx.compose.foundation.Image
import com.example.magpie_pinecone.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier, onClickGetStarted: () -> Unit) {

    // Background image is from unsplash: https://unsplash.com/photos/looking-up-through-dense-green-trees-to-a-cloudy-sky-y2pCSM6OnT4
    Box(modifier = Modifier.fillMaxSize()) {

        // Background image
        Image(
            painter = painterResource(id = R.drawable.pinecone_welcome_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            colorFilter = ColorFilter.tint(
                color = Color.Black.copy(alpha = 0.25f),
                blendMode = BlendMode.SrcOver
            )
        )
        // Ui components
        Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("Welcome to", fontSize = 36.sp, color = Color.White)
            Text("Pinecone", fontSize = 56.sp, color = Color.White)

            Spacer(modifier = Modifier.height(320.dp))

            Button(modifier = Modifier.size(width = 180.dp, height = 60.dp),
                onClick = onClickGetStarted,
                colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6155f5),
                contentColor = Color.White
            ), shape = RoundedCornerShape(8.dp)
                ) {
                Text("Get started", fontSize = 20.sp)
            }
        }
    }
}

@Preview (
    showBackground = true,
    showSystemUi = true
)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(onClickGetStarted = {})
}