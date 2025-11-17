package com.dev.marcelo.devlanguages.core.navigation

import kotlinx.serialization.Serializable

/**
 * Navigation Routes - Type-safe navigation with kotlinx.serialization
 *
 * Todas as rotas do aplicativo usando sealed interface para navegação segura.
 * Navigation Compose KMP suporta deep linking e type-safety.
 */

sealed interface Screen {

    // ===== Authentication Flow =====

    @Serializable
    data object Login : Screen

    @Serializable
    data object SignUp : Screen

    // ===== Onboarding Flow =====

    @Serializable
    data object LanguageSelection : Screen

    @Serializable
    data object Tutorial : Screen

    // ===== Main App Flow =====

    @Serializable
    data object Home : Screen

    @Serializable
    data object Profile : Screen

    // ===== Game Generation Flow =====

    /**
     * Tela de loading enquanto LLM gera os jogos
     * @param prompt O prompt do usuário (ex: "Quero aprender inglês - past tense")
     * @param language Língua alvo (ex: "english", "french")
     */
    @Serializable
    data class Generating(
        val prompt: String,
        val language: String
    ) : Screen

    /**
     * Tela de explicação do conteúdo antes dos jogos
     * @param sessionId ID da sessão de jogos no Firestore
     */
    @Serializable
    data class Explanation(
        val sessionId: String
    ) : Screen

    // ===== Game Flow =====

    /**
     * Container que gerencia o fluxo entre os jogos
     * @param sessionId ID da sessão de jogos
     */
    @Serializable
    data class GameContainer(
        val sessionId: String
    ) : Screen

    /**
     * Jogo de Matching (associação)
     * @param gameId ID do jogo específico
     * @param sessionId ID da sessão
     */
    @Serializable
    data class MatchingGame(
        val gameId: String,
        val sessionId: String
    ) : Screen

    /**
     * Jogo de Translation (tradução)
     * @param gameId ID do jogo específico
     * @param sessionId ID da sessão
     */
    @Serializable
    data class TranslationGame(
        val gameId: String,
        val sessionId: String
    ) : Screen

    /**
     * Jogo de Fill in the Blanks (preencher lacunas)
     * @param gameId ID do jogo específico
     * @param sessionId ID da sessão
     */
    @Serializable
    data class FillBlanksGame(
        val gameId: String,
        val sessionId: String
    ) : Screen

    // ===== Results Flow =====

    /**
     * Tela de resultados/score final
     * @param sessionId ID da sessão completada
     */
    @Serializable
    data class Score(
        val sessionId: String
    ) : Screen

    // ===== Subscription Flow =====

    /**
     * Tela de paywall (quando atinge limite free)
     */
    @Serializable
    data object Paywall : Screen

    /**
     * Tela de detalhes de assinatura/planos
     */
    @Serializable
    data object Subscription : Screen

    // ===== Settings Flow =====

    @Serializable
    data object Settings : Screen
}

/**
 * Root Screen - Tela inicial do app (decide entre Login ou Home)
 */
@Serializable
data object RootScreen

/**
 * Extension para obter o nome da rota (útil para analytics)
 */
val Screen.routeName: String
    get() = when (this) {
        is Screen.Login -> "login"
        is Screen.SignUp -> "signup"
        is Screen.LanguageSelection -> "language_selection"
        is Screen.Tutorial -> "tutorial"
        is Screen.Home -> "home"
        is Screen.Profile -> "profile"
        is Screen.Generating -> "generating"
        is Screen.Explanation -> "explanation"
        is Screen.GameContainer -> "game_container"
        is Screen.MatchingGame -> "matching_game"
        is Screen.TranslationGame -> "translation_game"
        is Screen.FillBlanksGame -> "fill_blanks_game"
        is Screen.Score -> "score"
        is Screen.Paywall -> "paywall"
        is Screen.Subscription -> "subscription"
        is Screen.Settings -> "settings"
    }
