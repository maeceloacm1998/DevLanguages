package com.dev.marcelo.devlanguages.core.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptions

/**
 * Navigation Extensions
 * Funções helper para facilitar navegação no app
 */

/**
 * Navega para uma Screen com opções customizáveis
 */
fun NavHostController.navigateTo(
    screen: Screen,
    navOptions: NavOptions? = null
) {
    navigate(screen, navOptions)
}

/**
 * Navega para uma Screen e limpa o back stack
 * Útil para: Login -> Home (não voltar para login)
 */
fun NavHostController.navigateAndClearBackStack(
    screen: Screen
) {
    navigate(screen) {
        popUpTo(0) {
            inclusive = true
        }
    }
}

/**
 * Navega para uma Screen e remove a tela anterior
 * Útil para: Generating -> Explanation (não voltar para loading)
 */
fun NavHostController.navigateAndPopCurrent(
    screen: Screen
) {
    navigate(screen) {
        popUpTo(currentBackStackEntry?.destination?.route ?: return@navigate) {
            inclusive = true
        }
    }
}

/**
 * Navega para Home e limpa todo o back stack
 * Útil após logout ou conclusão de onboarding
 */
fun NavHostController.navigateToHome() {
    navigate(Screen.Home) {
        popUpTo(0) {
            inclusive = true
        }
    }
}

/**
 * Navega para Login e limpa todo o back stack
 * Útil após logout
 */
fun NavHostController.navigateToLogin() {
    navigate(Screen.Login) {
        popUpTo(0) {
            inclusive = true
        }
    }
}

/**
 * Volta para a tela anterior se possível
 * @return true se conseguiu voltar, false caso contrário
 */
fun NavHostController.navigateBack(): Boolean {
    return if (previousBackStackEntry != null) {
        popBackStack()
        true
    } else {
        false
    }
}

/**
 * Navega para Profile
 */
fun NavHostController.navigateToProfile() {
    navigateTo(Screen.Profile)
}

/**
 * Navega para Settings
 */
fun NavHostController.navigateToSettings() {
    navigateTo(Screen.Settings)
}

/**
 * Navega para tela de geração de jogos
 * @param prompt Prompt do usuário
 * @param language Língua alvo
 */
fun NavHostController.navigateToGenerating(
    prompt: String,
    language: String
) {
    navigateTo(Screen.Generating(prompt, language))
}

/**
 * Navega para tela de explicação
 * Remove a tela de loading anterior
 */
fun NavHostController.navigateToExplanation(sessionId: String) {
    navigateAndPopCurrent(Screen.Explanation(sessionId))
}

/**
 * Navega para container de jogos
 * Remove a tela de explicação anterior
 */
fun NavHostController.navigateToGameContainer(sessionId: String) {
    navigateAndPopCurrent(Screen.GameContainer(sessionId))
}

/**
 * Navega para tela de score/resultados
 * Remove todo o fluxo de jogos do back stack
 */
fun NavHostController.navigateToScore(sessionId: String) {
    navigate(Screen.Score(sessionId)) {
        // Remove todas as telas de jogos
        popUpTo(Screen.Home) {
            inclusive = false
        }
    }
}

/**
 * Navega para paywall quando atinge limite
 */
fun NavHostController.navigateToPaywall() {
    navigateTo(Screen.Paywall)
}

/**
 * Navega para tela de assinatura/planos
 */
fun NavHostController.navigateToSubscription() {
    navigateTo(Screen.Subscription)
}

/**
 * Verifica se pode fazer back navigation
 */
val NavHostController.canNavigateBack: Boolean
    get() = previousBackStackEntry != null
