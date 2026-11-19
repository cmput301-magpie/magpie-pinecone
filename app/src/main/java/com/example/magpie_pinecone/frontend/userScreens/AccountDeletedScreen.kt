package com.example.magpie_pinecone.frontend.userScreens

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.ui.components.PillButton
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme

/**
 * Confirms that the user's account has been deleted.
 *
 * The user can return to the welcome screen to begin onboarding again.
 *
 * @param onBackToWelcome Called when the user taps Back To Welcome.
 */
@Composable
fun AccountDeletedScreen(
    onBackToWelcome: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        PineconeTopBar(
            title = "Account Deleted",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(
                    horizontal = 24.dp,
                    vertical = 8.dp
                )
        )

        Text(
            text = "We are sad to\nsee you go",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = maxHeight * 0.42f)
        )

        PillButton(
            text = "Back To Welcome",
            onClick = onBackToWelcome,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = maxHeight * 0.70f)
                .height(56.dp)
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AccountDeletedScreenPreview() {
    MagpiepineconeTheme {
        AccountDeletedScreen(
            onBackToWelcome = {}
        )
    }
}