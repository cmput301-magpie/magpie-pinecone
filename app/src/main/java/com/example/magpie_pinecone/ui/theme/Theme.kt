package com.example.magpie_pinecone.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light mode: the Figma colors
private val PineconeLightColorScheme = lightColorScheme(
    primary = PineconePurple,            // default Button color
    onPrimary = Color.White,             // text/icons on top of primary
    secondary = PillPurple,              // pill buttons (Edit Profile, Download CSV)
    onSecondary = Color.White,
    error = DeclineRed,
    onError = Color.White,
    background = Color.White,            // screen background
    surface = Color.White,
    surfaceContainer = LavenderSurface,  // bottom nav bar color
    outlineVariant = OutlineLavender     // card outlines and dividers
)

// Dark mode: used when the phone is set to dark theme
private val PineconeDarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    error = DarkError,
    onError = DarkOnError,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkBackground,
    onSurface = DarkOnBackground,
    surfaceContainer = DarkSurfaceContainer,  // bottom nav bar color in dark mode
    outlineVariant = DarkOutline
)

/**
 * The app's theme. Wrap every screen in this (MainActivity already does).
 *
 * darkTheme follows the phone's setting by default.
 * Dynamic (wallpaper) colors are turned off, so the app always uses the colors above.
 */
@Composable
fun MagpiepineconeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Pick the dark or light colors depending on the phone's setting
    val colorScheme = if (darkTheme) PineconeDarkColorScheme else PineconeLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}