package com.waymate.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// STRICT: Cream is the dominant background & surface, Green is primary, Daisy secondary, Sky tertiary
private val WayMateLightColorScheme = lightColorScheme(
    primary = WayMateGreen,
    onPrimary = WayMateOnGreen,
    primaryContainer = WayMateGreenContainer,
    onPrimaryContainer = WayMateGreenDark,

    secondary = WayMateDaisy,
    onSecondary = WayMateOnDaisy,
    secondaryContainer = WayMateDaisyContainer,
    onSecondaryContainer = WayMateDaisyDark,

    tertiary = WayMateSky,
    onTertiary = WayMateOnSky,
    tertiaryContainer = WayMateSkyContainer,
    onTertiaryContainer = WayMateSkyDark,

    background = WayMateCream,
    onBackground = WayMateTextPrimary,
    surface = WayMateCreamSurface,
    onSurface = WayMateTextPrimary,
    surfaceVariant = WayMateSurfaceVariant,
    onSurfaceVariant = WayMateTextSecondary,

    outline = WayMateBorder,
    outlineVariant = WayMateDivider,
    error = WayMateError,
    onError = WayMateWhite
)

private val WayMateDarkColorScheme = darkColorScheme(
    primary = WayMateGreenLight,
    onPrimary = WayMateTextPrimary,
    primaryContainer = WayMateGreenDark,
    onPrimaryContainer = WayMateGreenContainer,

    secondary = WayMateDaisyLight,
    onSecondary = WayMateTextPrimary,
    secondaryContainer = WayMateDaisyDark,
    onSecondaryContainer = WayMateDaisyContainer,

    tertiary = WayMateSkyLight,
    onTertiary = WayMateTextPrimary,
    tertiaryContainer = WayMateSkyDark,
    onTertiaryContainer = WayMateSkyContainer,

    background = WayMateTextPrimary,
    onBackground = WayMateCream,
    surface = Color(0xFF232B1E),
    onSurface = WayMateCreamLight,
    surfaceVariant = Color(0xFF2E3827),
    onSurfaceVariant = WayMateCreamDark,

    outline = Color(0xFF45513D),
    outlineVariant = Color(0xFF384332),
    error = WayMateError,
    onError = WayMateWhite
)

@Composable
fun WayMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) WayMateDarkColorScheme else WayMateLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.surface.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = !darkTheme
                controller.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
