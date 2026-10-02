package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MonaymPrimary,
    onPrimary = Color.White,
    secondary = MonaymGold,
    onSecondary = Color.Black,
    tertiary = MonaymGreen,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = TextOnDarkPrimary,
    onSurface = TextOnDarkPrimary,
    surfaceVariant = DarkSurface,
    onSurfaceVariant = TextOnDarkPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = MonaymPrimary,
    onPrimary = Color.White,
    secondary = MonaymPrimaryDark,
    onSecondary = Color.White,
    tertiary = MonaymGold,
    background = LightBackground,
    surface = LightSurface,
    onBackground = TextOnWhitePrimary,
    onSurface = TextOnWhitePrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextOnWhiteSecondary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
