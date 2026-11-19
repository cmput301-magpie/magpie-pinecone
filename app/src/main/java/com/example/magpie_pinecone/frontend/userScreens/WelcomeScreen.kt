package com.example.magpie_pinecone.frontend.userScreens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.R
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme

// Welcome screen specific colors from the Figma design.
private val WelcomeGradientTop = Color(0xFF75E6E7)
private val WelcomeGradientBottom = Color(0xFF2D6EA7)

/**
 * Displays the first screen shown to a new user.
 *
 * Tapping Get Started opens the profile setup screen.
 *
 * @param modifier Modifier applied to the welcome screen.
 * @param onClickGetStarted Called when the user taps Get Started.
 */
@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onClickGetStarted: () -> Unit
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val headerHeight = maxHeight * 0.49f
        val imageHeight = maxHeight * 0.65f

        // Mountain and lake image shown below the welcome banner.
        Image(
            painter = painterResource(
                id = R.drawable.pinecone_welcome_background
            ),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(imageHeight),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter
        )

        // Single curved cyan-to-blue banner from the Figma welcome screen.
        Canvas(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(headerHeight)
        ) {
            val width = size.width
            val height = size.height

            val headerShape = Path().apply {
                moveTo(0f, 0f)
                lineTo(width, 0f)

                // Raise the right edge so more of the mountain is visible.
                lineTo(
                    width,
                    height * 0.78f
                )

                cubicTo(
                    width * 0.82f,
                    height * 0.84f,
                    width * 0.70f,
                    height * 0.95f,
                    width * 0.58f,
                    height * 0.98f
                )

                cubicTo(
                    width * 0.48f,
                    height * 1.00f,
                    width * 0.39f,
                    height * 0.95f,
                    width * 0.29f,
                    height * 0.91f
                )

                cubicTo(
                    width * 0.19f,
                    height * 0.87f,
                    width * 0.10f,
                    height * 0.93f,
                    0f,
                    height * 0.97f
                )

                close()
            }

            drawPath(
                path = headerShape,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        WelcomeGradientTop,
                        WelcomeGradientBottom
                    ),
                    startY = 0f,
                    endY = height
                )
            )
        }

        // Welcome title.
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(
                    top = maxHeight * 0.17f
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Pinecone",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.displayMedium
            )
        }

        // Continue to profile setup.
        Button(
            onClick = onClickGetStarted,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(
                    top = maxHeight * 0.64f
                )
                .fillMaxWidth(0.65f)
                .widthIn(
                    max = 300.dp
                )
                .height(58.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp
            )
        ) {
            Text(
                text = "Get Started",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun WelcomeScreenPreview() {
    MagpiepineconeTheme {
        WelcomeScreen(
            onClickGetStarted = {}
        )
    }
}