package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Display's App Policy and Guidelines
 * TO-DO
 * Add an actual policy and guideline
 */

@Composable
fun AppPolicyGuidelinesScreen(
    onBack: () -> Boolean
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

            Button(
                modifier = Modifier.align(Alignment.Start),
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
                    text = "App Policy and Guidelines",
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center
                )

                // App Policy and Guidelines
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc sit amet tellus bibendum, tristique orci sit amet, ornare neque. Cras mauris nisi, maximus id arcu in, dapibus rutrum urna. Nulla eget felis vel odio semper dapibus non vitae urna. Mauris quis commodo tellus. Ut quis est vulputate massa efficitur laoreet. Quisque ac maximus neque, quis consequat sapien. In non sagittis odio, sit amet ullamcorper dolor.",
                    fontSize = 16.sp
                )
            }


    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowAppPolicyGuidelinesScreenPreview() {
    AppPolicyGuidelinesScreen({ false })
}