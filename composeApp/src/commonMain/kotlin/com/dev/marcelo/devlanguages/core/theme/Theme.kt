package com.dev.marcelo.devlanguages.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * DevLanguages Theme
 * Tema principal do aplicativo com suporte a Light e Dark mode
 */

// Light Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = NeutralWhite,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = PrimaryDark,

    secondary = Secondary,
    onSecondary = NeutralWhite,
    secondaryContainer = SecondaryLight,
    onSecondaryContainer = SecondaryDark,

    tertiary = Accent,
    onTertiary = NeutralWhite,
    tertiaryContainer = AccentLight,
    onTertiaryContainer = AccentDark,

    error = Error,
    onError = NeutralWhite,
    errorContainer = ErrorDark,
    onErrorContainer = NeutralWhite,

    background = BackgroundLight,
    onBackground = TextPrimaryLight,

    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,

    outline = NeutralGray300,
    outlineVariant = NeutralGray200,

    scrim = NeutralBlack.copy(alpha = 0.32f),

    inverseSurface = NeutralGray800,
    inverseOnSurface = NeutralWhite,
    inversePrimary = PrimaryLight,

    surfaceTint = Primary
)

// Dark Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLight,
    onPrimary = NeutralGray900,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = PrimaryLight,

    secondary = SecondaryLight,
    onSecondary = NeutralGray900,
    secondaryContainer = SecondaryDark,
    onSecondaryContainer = SecondaryLight,

    tertiary = AccentLight,
    onTertiary = NeutralGray900,
    tertiaryContainer = AccentDark,
    onTertiaryContainer = AccentLight,

    error = Error,
    onError = NeutralGray900,
    errorContainer = ErrorDark,
    onErrorContainer = Error,

    background = BackgroundDark,
    onBackground = TextPrimaryDark,

    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,

    outline = NeutralGray700,
    outlineVariant = NeutralGray800,

    scrim = NeutralBlack.copy(alpha = 0.32f),

    inverseSurface = NeutralGray200,
    inverseOnSurface = NeutralGray900,
    inversePrimary = Primary,

    surfaceTint = PrimaryLight
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
