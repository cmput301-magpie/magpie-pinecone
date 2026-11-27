package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.ui.components.PillButton
import com.example.magpie_pinecone.ui.components.PineconeTextField
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.example.magpie_pinecone.ui.components.StarRatingInput
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import com.example.magpie_pinecone.ui.theme.PineconePurple

/*
 * WriteReviewScreen.kt contains all relevant functions for displaying the screen to write a campsite review.
 *
 * Composable Functions:
 * WriteReviewScreen displays the form where an entrant gives a star rating and writes a comment.
 * ShowWriteReviewScreenPreview displays a preview of WriteReviewScreen for reference.
 */

// Longest comment allowed
private const val MAX_COMMENT_LENGTH = 1000

/**
 * Displays the form for writing or editing a campsite review.
 * Submit stays disabled until a star rating is chosen and the comment is not blank.
 *
 * @param campsiteName Name of the campsite being reviewed.
 * @param initialRating Rating to start with (0 for none), used when editing a review.
 * @param initialComment Comment to start with, used when editing a review.
 * @param isEdit Whether the entrant is editing a review they already posted.
 * @param onBack Returns to the previous screen.
 * @param onSubmit Called with the chosen rating and the comment when the entrant submits.
 */
@Composable
fun WriteReviewScreen(
    campsiteName: String,
    initialRating: Int = 0,
    initialComment: String = "",
    isEdit: Boolean = false,
    onBack: () -> Unit,
    onSubmit: (rating: Int, comment: String) -> Unit
) {
    var rating by remember { mutableIntStateOf(initialRating) }
    var comment by remember { mutableStateOf(initialComment) }

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
        // The form scrolls so the keyboard doesn't hide it, while Submit stays pinned
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PineconeTopBar(
                title = if (isEdit) "Edit review" else "Write a review",
                onBack = onBack
            )

            Text(
                text = campsiteName,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Your rating",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            StarRatingInput(
                rating = rating,
                onRatingChange = { rating = it }
            )

            PineconeTextField(
                value = comment,
                onValueChange = { if (it.length <= MAX_COMMENT_LENGTH) comment = it },
                label = "Your review",
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Text(
                text = "${comment.length}/$MAX_COMMENT_LENGTH",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        PillButton(
            text = if (isEdit) "Update review" else "Submit review",
            onClick = { onSubmit(rating, comment.trim()) },
            containerColor = PineconePurple,
            enabled = rating in 1..5 && comment.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(52.dp)
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ShowWriteReviewScreenPreview() {
    MagpiepineconeTheme {
        WriteReviewScreen(
            campsiteName = "Sample Campsite",
            onBack = {},
            onSubmit = { _, _ -> }
        )
    }
}