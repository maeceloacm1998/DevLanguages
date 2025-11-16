package com.dev.marcelo.devlanguages.core.utils

/**
 * Validation Utilities
 * Funções para validação de inputs do usuário
 */

object ValidationUtils {

    /**
     * Valida email
     * @return null se válido, mensagem de erro caso contrário
     */
    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "Email não pode estar vazio"
            !email.isValidEmail() -> "Email inválido"
            else -> null
        }
    }

    /**
     * Valida senha
     * Regras: mínimo 6 caracteres
     * @return null se válido, mensagem de erro caso contrário
     */
    fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Senha não pode estar vazia"
            password.length < 6 -> "Senha deve ter no mínimo 6 caracteres"
            else -> null
        }
    }

    /**
     * Valida confirmação de senha
     * @return null se válido, mensagem de erro caso contrário
     */
    fun validatePasswordConfirmation(password: String, confirmation: String): String? {
        return when {
            confirmation.isBlank() -> "Confirmação não pode estar vazia"
            password != confirmation -> "As senhas não coincidem"
            else -> null
        }
    }

    /**
     * Valida nome (não vazio, mínimo 2 caracteres)
     * @return null se válido, mensagem de erro caso contrário
     */
    fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "Nome não pode estar vazio"
            name.trim().length < 2 -> "Nome deve ter no mínimo 2 caracteres"
            !name.all { it.isLetter() || it.isWhitespace() } -> "Nome deve conter apenas letras"
            else -> null
        }
    }

    /**
     * Valida prompt do usuário (para geração de jogos)
     * @return null se válido, mensagem de erro caso contrário
     */
    fun validatePrompt(prompt: String, minLength: Int = 10, maxLength: Int = 500): String? {
        return when {
            prompt.isBlank() -> "Digite o que você quer aprender"
            prompt.trim().length < minLength -> "Seja mais específico (mínimo $minLength caracteres)"
            prompt.length > maxLength -> "Prompt muito longo (máximo $maxLength caracteres)"
            else -> null
        }
    }

    /**
     * Valida resposta de jogo (não vazio)
     * @return null se válido, mensagem de erro caso contrário
     */
    fun validateGameAnswer(answer: String): String? {
        return when {
            answer.isBlank() -> "Resposta não pode estar vazia"
            else -> null
        }
    }

    /**
     * Valida campo de texto genérico
     * @return null se válido, mensagem de erro caso contrário
     */
    fun validateTextField(
        value: String,
        fieldName: String = "Campo",
        minLength: Int = 1,
        maxLength: Int = Int.MAX_VALUE,
        required: Boolean = true
    ): String? {
        return when {
            required && value.isBlank() -> "$fieldName não pode estar vazio"
            value.length < minLength -> "$fieldName deve ter no mínimo $minLength caracteres"
            value.length > maxLength -> "$fieldName deve ter no máximo $maxLength caracteres"
            else -> null
        }
    }

    /**
     * Valida CPF (apenas formato, não valida dígitos verificadores)
     * @return null se válido, mensagem de erro caso contrário
     */
    fun validateCPF(cpf: String): String? {
        val numbers = cpf.onlyDigits()
        return when {
            numbers.isBlank() -> "CPF não pode estar vazio"
            numbers.length != 11 -> "CPF deve ter 11 dígitos"
            numbers.all { it == numbers[0] } -> "CPF inválido" // 111.111.111-11
            else -> null // Validação completa com dígitos verificadores pode ser adicionada
        }
    }

    /**
     * Valida telefone brasileiro (10 ou 11 dígitos)
     * @return null se válido, mensagem de erro caso contrário
     */
    fun validatePhone(phone: String): String? {
        val numbers = phone.onlyDigits()
        return when {
            numbers.isBlank() -> "Telefone não pode estar vazio"
            numbers.length !in 10..11 -> "Telefone inválido"
            else -> null
        }
    }

    /**
     * Valida URL
     * @return null se válido, mensagem de erro caso contrário
     */
    fun validateUrl(url: String): String? {
        val urlRegex = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$".toRegex()
        return when {
            url.isBlank() -> "URL não pode estar vazia"
            !url.matches(urlRegex) -> "URL inválida"
            else -> null
        }
    }
}

/**
 * Sealed class para resultados de validação
 */
sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val errorMessage: String) : ValidationResult()
}

/**
 * Extension para validar campo com resultado tipado
 */
fun String.validate(validator: (String) -> String?): ValidationResult {
    val error = validator(this)
    return if (error == null) {
        ValidationResult.Valid
    } else {
        ValidationResult.Invalid(error)
    }
}

/**
 * Extension para verificar se ValidationResult é válido
 */
val ValidationResult.isValid: Boolean
    get() = this is ValidationResult.Valid

/**
 * Extension para obter mensagem de erro
 */
val ValidationResult.errorMessage: String?
    get() = (this as? ValidationResult.Invalid)?.errorMessage
