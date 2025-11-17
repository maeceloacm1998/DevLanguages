package com.dev.marcelo.devlanguages.features.onboarding.domain.model

/**
 * Language
 * Modelo de linguagem disponível para aprendizado
 */
data class Language(
    val code: String,           // "en", "es", "fr", etc.
    val name: String,           // "English", "Spanish", "French"
    val nativeName: String,     // "English", "Español", "Français"
    val flagEmoji: String,      // "🇺🇸", "🇪🇸", "🇫🇷"
    val isPopular: Boolean = false  // Para destacar línguas populares
)

/**
 * Linguagens pré-definidas disponíveis no app
 */
object AvailableLanguages {
    val ENGLISH = Language(
        code = "en",
        name = "Inglês",
        nativeName = "English",
        flagEmoji = "🇺🇸",
        isPopular = true
    )

    val SPANISH = Language(
        code = "es",
        name = "Espanhol",
        nativeName = "Español",
        flagEmoji = "🇪🇸",
        isPopular = true
    )

    val FRENCH = Language(
        code = "fr",
        name = "Francês",
        nativeName = "Français",
        flagEmoji = "🇫🇷",
        isPopular = true
    )

    val GERMAN = Language(
        code = "de",
        name = "Alemão",
        nativeName = "Deutsch",
        flagEmoji = "🇩🇪",
        isPopular = false
    )

    val ITALIAN = Language(
        code = "it",
        name = "Italiano",
        nativeName = "Italiano",
        flagEmoji = "🇮🇹",
        isPopular = false
    )

    val PORTUGUESE = Language(
        code = "pt",
        name = "Português",
        nativeName = "Português",
        flagEmoji = "🇵🇹",
        isPopular = false
    )

    val JAPANESE = Language(
        code = "ja",
        name = "Japonês",
        nativeName = "日本語",
        flagEmoji = "🇯🇵",
        isPopular = true
    )

    val CHINESE = Language(
        code = "zh",
        name = "Chinês",
        nativeName = "中文",
        flagEmoji = "🇨🇳",
        isPopular = false
    )

    val KOREAN = Language(
        code = "ko",
        name = "Coreano",
        nativeName = "한국어",
        flagEmoji = "🇰🇷",
        isPopular = false
    )

    val RUSSIAN = Language(
        code = "ru",
        name = "Russo",
        nativeName = "Русский",
        flagEmoji = "🇷🇺",
        isPopular = false
    )

    /**
     * Lista de todas as linguagens disponíveis
     */
    val all: List<Language> = listOf(
        ENGLISH,
        SPANISH,
        FRENCH,
        GERMAN,
        ITALIAN,
        PORTUGUESE,
        JAPANESE,
        CHINESE,
        KOREAN,
        RUSSIAN
    )

    /**
     * Lista de linguagens populares (destacadas na UI)
     */
    val popular: List<Language> = all.filter { it.isPopular }
}
