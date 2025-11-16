package com.dev.marcelo.devlanguages.core.theme.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Loading Indicator Component
 * Indicador de carregamento circular
 */
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    strokeWidth: Dp = 4.dp
) {
    CircularProgressIndicator(
        modifier = modifier.size(size),
        color = color,
        strokeWidth = strokeWidth
    )
}

/**
 * Fullscreen Loading Indicator
 * Para telas de loading em tela cheia
 */
@Composable
fun FullscreenLoading(
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        LoadingIndicator(
            size = 48.dp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// Import adicional necessário
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
