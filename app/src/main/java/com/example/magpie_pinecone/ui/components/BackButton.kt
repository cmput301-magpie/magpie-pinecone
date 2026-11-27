package com.example.magpie_pinecone.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.ui.theme.PineconePurple

/*
 * BackButton.kt contains all relevant functions for a back button used in the app.
 *
 * Composable Functions:
 * BackButton displays a back button, for use in all screens needing it for UI consistency.
 * PreviewBackButton displays a preview of BackButton for reference.
 */

@Composable
fun BackButton(modifier: Modifier = Modifier, onBack: () -> Unit) {
    IconButton(
        modifier = modifier.size(50.dp),
        onClick = { onBack() },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = PineconePurple,
            contentColor = Color.White
        )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back Button",
            tint = Color.White
        )
    }
}

@Preview
@Composable
fun PreviewBackButton() {
    BackButton(modifier = Modifier.width(50.dp).height(50.dp), { true })
}