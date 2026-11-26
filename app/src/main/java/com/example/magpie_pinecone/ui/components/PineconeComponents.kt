package com.example.magpie_pinecone.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.R
import com.example.magpie_pinecone.frontend.sharedComposables.BackButton
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import com.example.magpie_pinecone.ui.theme.StarGold

/*
 * PineconeComponents.kt contains an assortment of composable functions for use across the app's screens.
 *
 * Composable Functions:
 * PineconeTopBar displays a title and back button for use across many screens.
 * PineconeTextField displays a text field for use across many screens.
 * PillButton displays a rounded button for use across many screens.
 * StarRating displays a row of 5 stars for use in ratings.
 * CampsiteImage displays an image, with a placeholder being a stock campsite image.
 * PineconeComponentsPreview displays a preview of the assorted components for reference.
 */

/**
 * Top bar used on every screen: back arrow on the left, title in the middle.
 * Leave onBack as null for screens with no back arrow.
 * If your screen's onBack returns a Boolean, pass it like this: onBack = { onBack() }
 */
@Composable
fun PineconeTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (onBack != null) {
            BackButton(onBack = onBack, modifier = Modifier.align(Alignment.CenterStart))
        }
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
    }
}

/**
 * Outlined text field with a clear (x) button on the right, like the Figma fields.
 * Set singleLine = false for long fields like Description.
 */
@Composable
fun PineconeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        trailingIcon = {
            // Only show the clear button when there is text to clear
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Outlined.Cancel, contentDescription = "Clear")
                }
            }
        }
    )
}

/**
 * Rounded "pill" button with an optional icon on the left.
 * Default color is the pill purple. Pass ReserveGreen / DeclineRed for other buttons.
 */
@Composable
fun PillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    containerColor: Color = MaterialTheme.colorScheme.secondary,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(50),   // 50% makes the ends fully round
        colors = ButtonDefaults.buttonColors(containerColor = containerColor)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text)
    }
}

/**
 * Five gold stars plus the number, e.g. "5.0".
 * Campsite.rating is a Double, so pass campsite.rating directly.
 */
@Composable
fun StarRating(rating: Double, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                contentDescription = null,
                tint = StarGold,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "%.1f".format(rating), style = MaterialTheme.typography.labelSmall)
    }
}

/**
 * Campsite picture with rounded corners.
 * Uses the placeholder drawable until real images are stored.
 */
@Composable
fun CampsiteImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.placeholdercampsiteimage),
        contentDescription = "Campsite image",
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(RoundedCornerShape(8.dp))
    )
}

// Preview so you can check the components in Android Studio without running the app
@Preview(showBackground = true)
@Composable
fun PineconeComponentsPreview() {
    MagpiepineconeTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PineconeTopBar(title = "Profile", onBack = {})
            PineconeTextField(value = "David", onValueChange = {}, label = "First name")
            PillButton(text = "Edit Profile", onClick = {}, icon = Icons.Filled.Edit)
            StarRating(rating = 5.0)
            CampsiteImage(modifier = Modifier.size(120.dp))
        }
    }
}