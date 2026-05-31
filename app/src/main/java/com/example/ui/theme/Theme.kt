package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CosmicColorScheme = darkColorScheme(
    primary = MoonstoneBlue,
    secondary = MoonstoneSoft,
    tertiary = MoonstoneIce,
    background = ObsidianBlack,
    surface = GraphiteCharcoal,
    onPrimary = ObsidianBlack,
    onSecondary = ObsidianBlack,
    onTertiary = ObsidianBlack,
    onBackground = OffWhite,
    onSurface = OffWhite,
    surfaceVariant = ElevatedBlack
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CosmicColorScheme,
        typography = Typography,
        content = content
    )
}
