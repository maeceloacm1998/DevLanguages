package com.dev.marcelo.devlanguages.core.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * DevLanguages Spacing System
 * Sistema de espaçamentos consistente baseado em múltiplos de 4dp
 */

object Spacing {
    // Base spacing unit (4dp)
    val base: Dp = 4.dp

    // Extra Small
    val extraSmall: Dp = 4.dp      // 4dp
    val small: Dp = 8.dp            // 8dp

    // Medium
    val medium: Dp = 16.dp          // 16dp
    val mediumLarge: Dp = 20.dp     // 20dp

    // Large
    val large: Dp = 24.dp           // 24dp
    val extraLarge: Dp = 32.dp      // 32dp

    // Extra Large
    val huge: Dp = 48.dp            // 48dp
    val massive: Dp = 64.dp         // 64dp

    // Specific use cases
    val buttonPaddingVertical: Dp = 12.dp
    val buttonPaddingHorizontal: Dp = 24.dp
    val cardPadding: Dp = 16.dp
    val screenPadding: Dp = 16.dp
    val iconSize: Dp = 24.dp
    val iconSizeSmall: Dp = 16.dp
    val iconSizeLarge: Dp = 32.dp
}

/**
 * Border Radius / Corner Sizes
 */
object CornerRadius {
    val none: Dp = 0.dp
    val small: Dp = 4.dp
    val medium: Dp = 8.dp
    val large: Dp = 12.dp
    val extraLarge: Dp = 16.dp
    val full: Dp = 999.dp // Para círculos/pills
}

/**
 * Elevation / Shadow
 */
object Elevation {
    val none: Dp = 0.dp
    val small: Dp = 2.dp
    val medium: Dp = 4.dp
    val large: Dp = 8.dp
    val extraLarge: Dp = 16.dp
}

/**
 * Border Widths
 */
object BorderWidth {
    val thin: Dp = 1.dp
    val medium: Dp = 2.dp
    val thick: Dp = 4.dp
}
