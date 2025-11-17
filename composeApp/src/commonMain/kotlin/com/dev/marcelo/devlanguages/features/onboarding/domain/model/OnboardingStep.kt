package com.dev.marcelo.devlanguages.features.onboarding.domain.model

/**
 * OnboardingStep
 * Representa uma etapa do tutorial de onboarding
 */
data class OnboardingStep(
    val id: Int,
    val title: String,
    val description: String,
    val imageResource: String? = null  // Caminho para ilustração (opcional)
)

/**
 * Passos do tutorial de onboarding
 */
object OnboardingSteps {
    val WELCOME = OnboardingStep(
        id = 0,
        title = "Bem-vindo ao DevLanguages!",
        description = "Aprenda idiomas de forma divertida e gamificada com a ajuda da inteligência artificial."
    )

    val PERSONALIZED_LEARNING = OnboardingStep(
        id = 1,
        title = "Aprendizado Personalizado",
        description = "Digite o que você quer aprender e nossa IA cria jogos personalizados para você. Exemplo: 'Quero aprender verbos no passado em inglês'."
    )

    val GAMES = OnboardingStep(
        id = 2,
        title = "Jogos Divertidos",
        description = "Pratique com jogos interativos: tradução, completar frases, associação de palavras e muito mais!"
    )

    val PROGRESS = OnboardingStep(
        id = 3,
        title = "Acompanhe seu Progresso",
        description = "Ganhe pontos, acompanhe suas estatísticas e veja sua evolução em cada idioma."
    )

    /**
     * Lista de todos os passos do onboarding
     */
    val all: List<OnboardingStep> = listOf(
        WELCOME,
        PERSONALIZED_LEARNING,
        GAMES,
        PROGRESS
    )
}
