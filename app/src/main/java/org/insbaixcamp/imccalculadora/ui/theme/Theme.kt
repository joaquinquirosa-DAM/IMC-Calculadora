package org.insbaixcamp.imccalculadora.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val HackerColorScheme = darkColorScheme(
    primary = HackerGreen,
    onPrimary = HackerBg,
    primaryContainer = HackerGreenDark,
    onPrimaryContainer = HackerGreenBright,
    secondary = HackerCyan,
    onSecondary = HackerBg,
    tertiary = HackerYellow,
    background = HackerBg,
    onBackground = HackerGreen,
    surface = HackerSurface,
    onSurface = HackerGreen,
    surfaceVariant = HackerGray,
    onSurfaceVariant = HackerTextSecondary,
    outline = HackerBorder
)

@Composable
fun ImcCalculadoraTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = HackerColorScheme,
        typography = Typography,
        content = content
    )
}
