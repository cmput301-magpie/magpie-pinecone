package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import androidx.compose.ui.text.font.FontWeight

/**
 * Displays the app policy and guidelines.
 *
 * @param onBack Returns to the previous screen.
 */
@Composable
fun AppPolicyGuidelinesScreen(
    onBack: () -> Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PineconeTopBar(
            title = "App Policy",
            onBack = {
                onBack()
            }
        )

        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc aliquam mollis est in auctor. Quisque imperdiet scelerisque metus, id semper justo convallis eu. Maecenas a nisl dolor. Nunc sit amet gravida nulla, in accumsan urna. Morbi tempus mauris tristique, euismod felis ut, finibus odio. Fusce posuere gravida nisi, sed laoreet dolor consectetur eu. Nam congue id nulla in volutpat. Curabitur maximus sem a turpis fermentum fermentum. Sed sapien dolor, dignissim vitae dui vel, lobortis convallis est. Integer varius eros quis tincidunt ultrices. Morbi tempor venenatis nibh a efficitur. Nunc ac euismod massa. Donec mauris turpis, lobortis a blandit eu, sodales id velit.
    """.trimIndent(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 380.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AppPolicyGuidelinesScreenPreview() {
    MagpiepineconeTheme {
        AppPolicyGuidelinesScreen(
            onBack = { false }
        )
    }
}