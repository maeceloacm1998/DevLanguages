package com.dev.marcelo.devlanguages.core.utils

/**
 * String Extensions
 * Extensões úteis para manipulação de strings
 */

/**
 * Verifica se string é email válido (regex simples)
 */
fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
    return this.matches(emailRegex)
}

/**
 * Verifica se string não é vazia ou só espaços
 */
fun String.isNotBlank(): Boolean = this.isNotEmpty() && this.trim().isNotEmpty()

/**
 * Capitaliza a primeira letra
 */
fun String.capitalizeFirst(): String {
    return if (this.isEmpty()) this else this[0].uppercaseChar() + this.substring(1)
}

/**
 * Capitaliza cada palavra
 */
fun String.capitalizeWords(): String {
    return this.split(" ").joinToString(" ") { it.capitalizeFirst() }
}

/**
 * Trunca string até certo tamanho, adicionando "..."
 */
fun String.truncate(maxLength: Int, suffix: String = "..."): String {
    return if (this.length <= maxLength) {
        this
    } else {
        this.take(maxLength - suffix.length) + suffix
    }
}

/**
 * Remove acentos de uma string
 */
fun String.removeAccents(): String {
    val accents = mapOf(
        'á' to 'a', 'à' to 'a', 'â' to 'a', 'ã' to 'a', 'ä' to 'a',
        'é' to 'e', 'è' to 'e', 'ê' to 'e', 'ë' to 'e',
        'í' to 'i', 'ì' to 'i', 'î' to 'i', 'ï' to 'i',
        'ó' to 'o', 'ò' to 'o', 'ô' to 'o', 'õ' to 'o', 'ö' to 'o',
        'ú' to 'u', 'ù' to 'u', 'û' to 'u', 'ü' to 'u',
        'ç' to 'c', 'ñ' to 'n',
        'Á' to 'A', 'À' to 'A', 'Â' to 'A', 'Ã' to 'A', 'Ä' to 'A',
        'É' to 'E', 'È' to 'E', 'Ê' to 'E', 'Ë' to 'E',
        'Í' to 'I', 'Ì' to 'I', 'Î' to 'I', 'Ï' to 'I',
        'Ó' to 'O', 'Ò' to 'O', 'Ô' to 'O', 'Õ' to 'O', 'Ö' to 'O',
        'Ú' to 'U', 'Ù' to 'U', 'Û' to 'U', 'Ü' to 'U',
        'Ç' to 'C', 'Ñ' to 'N'
    )
    return this.map { accents[it] ?: it }.joinToString("")
}

/**
 * Máscara para telefone brasileiro: (XX) XXXXX-XXXX
 */
fun String.toPhoneMask(): String {
    val numbers = this.filter { it.isDigit() }
    return when (numbers.length) {
        11 -> "(${numbers.substring(0, 2)}) ${numbers.substring(2, 7)}-${numbers.substring(7)}"
        10 -> "(${numbers.substring(0, 2)}) ${numbers.substring(2, 6)}-${numbers.substring(6)}"
        else -> this
    }
}

/**
 * Remove todos os caracteres não numéricos
 */
fun String.onlyDigits(): String = this.filter { it.isDigit() }

/**
 * Verifica se contém apenas letras
 */
fun String.isAlphabetic(): Boolean = this.all { it.isLetter() }

/**
 * Verifica se contém apenas letras e números
 */
fun String.isAlphanumeric(): Boolean = this.all { it.isLetterOrDigit() }

/**
 * Conta palavras na string
 */
fun String.wordCount(): Int {
    return if (this.isBlank()) 0 else this.trim().split("\\s+".toRegex()).size
}

/**
 * Normaliza espaços (remove múltiplos espaços)
 */
fun String.normalizeSpaces(): String {
    return this.trim().replace("\\s+".toRegex(), " ")
}

/**
 * Máscara de CPF: XXX.XXX.XXX-XX
 */
fun String.toCpfMask(): String {
    val numbers = this.onlyDigits()
    return if (numbers.length == 11) {
        "${numbers.substring(0, 3)}.${numbers.substring(3, 6)}.${numbers.substring(6, 9)}-${numbers.substring(9)}"
    } else {
        this
    }
}

/**
 * Verifica se string é um número
 */
fun String.isNumeric(): Boolean = this.toDoubleOrNull() != null

/**
 * Retorna null se string estiver vazia, senão retorna a string
 */
fun String.toNullIfEmpty(): String? = if (this.isEmpty()) null else this

/**
 * Retorna null se string estiver em branco, senão retorna a string
 */
fun String.toNullIfBlank(): String? = if (this.isBlank()) null else this

/**
 * Compara strings ignorando case e acentos
 * Útil para comparação de respostas de jogos
 */
fun String.equalsIgnoreCaseAndAccents(other: String): Boolean {
    return this.removeAccents().lowercase() == other.removeAccents().lowercase()
}

/**
 * Verifica similaridade entre strings (útil para validação parcial de respostas)
 * Retorna porcentagem de similaridade (0.0 a 1.0)
 */
fun String.similarity(other: String): Double {
    val s1 = this.lowercase().removeAccents()
    val s2 = other.lowercase().removeAccents()

    if (s1 == s2) return 1.0
    if (s1.isEmpty() || s2.isEmpty()) return 0.0

    val longer = if (s1.length > s2.length) s1 else s2
    val shorter = if (s1.length > s2.length) s2 else s1

    // Conta caracteres em comum
    val common = shorter.count { longer.contains(it) }
    return common.toDouble() / longer.length
}
