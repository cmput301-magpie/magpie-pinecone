package com.example.magpie_pinecone.frontend.ownerScreens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.frontend.entrantScreens.CampsiteDetailsLayout
import com.example.magpie_pinecone.ui.components.PillButton
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import com.example.magpie_pinecone.ui.theme.PineconePurple

/*
 * ViewCampsiteScreen.kt contains all relevant functions for displaying a preview of the owner's campsite.
 *
 * Composable Functions:
 * ViewCampsiteScreen displays the owner's campsite exactly as entrants see it, with a button to edit it.
 * ShowViewCampsiteScreenPreview displays a preview of ViewCampsiteScreen for reference.
 */

/**
 * Shows the owner a preview of their campsite.
 *
 * Uses the same details page that entrants see (as in Figma), but with an
 * "Edit Campsite" button at the bottom instead of the lottery buttons.
 *
 * @param campsite The campsite being previewed.
 * @param onBack Returns to the previous screen.
 * @param onEdit Opens the campsite for editing.
 */
@Composable
fun ViewCampsiteScreen(
    campsite: Campsite,
    onBack: () -> Unit,
    onEdit: () -> Unit
) {
    CampsiteDetailsLayout(
        campsite = campsite,
        onBack = onBack
    ) {
        PillButton(
            text = "Edit Campsite",
            onClick = onEdit,
            icon = Icons.Filled.Edit,
            containerColor = PineconePurple,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ShowViewCampsiteScreenPreview() {
    MagpiepineconeTheme {
        ViewCampsiteScreen(
            campsite = Campsite(
                name = "Sample Campsite",
                location = "Edmonton",
                maxOccupancy = 5,
                price = 38.0,
                rating = 4.5,
                description = "A sample campsite used for previewing the owner's view.",
                tags = listOf("Forest", "Lake")
            ),
            onBack = { },
            onEdit = { }
        )
    }
}