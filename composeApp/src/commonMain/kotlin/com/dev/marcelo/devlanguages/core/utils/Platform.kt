package com.dev.marcelo.devlanguages.core.utils

/**
 * Platform Detection
 * Detecta a plataforma em que o app está rodando (Android ou iOS)
 */
expect object Platform {
    val isAndroid: Boolean
    val isIOS: Boolean
    val name: String
}
