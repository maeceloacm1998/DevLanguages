package com.dev.marcelo.devlanguages.core.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

/**
 * DateTime Utilities
 * Funções úteis para manipulação de datas e horas
 */

object DateTimeUtils {
    /**
     * Retorna o timestamp atual em milissegundos
     */
    fun now(): Long = Clock.System.now().toEpochMilliseconds()

    /**
     * Retorna o Instant atual
     */
    fun nowInstant(): Instant = Clock.System.now()

    /**
     * Retorna a data atual (sem hora)
     */
    fun today(): LocalDate {
        val now = Clock.System.now()
        return now.toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    /**
     * Retorna a data/hora atual no timezone local
     */
    fun nowLocalDateTime(): LocalDateTime {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    /**
     * Converte timestamp (millis) para LocalDateTime
     */
    fun fromMillis(millis: Long): LocalDateTime {
        return Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }

    /**
     * Verifica se duas datas são do mesmo dia
     */
    fun isSameDay(date1: LocalDate, date2: LocalDate): Boolean {
        return date1 == date2
    }

    /**
     * Verifica se é hoje
     */
    fun isToday(date: LocalDate): Boolean {
        return isSameDay(date, today())
    }

    /**
     * Formata data para string legível (ex: "16 Nov 2025")
     */
    fun formatDate(date: LocalDate): String {
        val month = when (date.monthNumber) {
            1 -> "Jan"
            2 -> "Fev"
            3 -> "Mar"
            4 -> "Abr"
            5 -> "Mai"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Ago"
            9 -> "Set"
            10 -> "Out"
            11 -> "Nov"
            12 -> "Dez"
            else -> ""
        }
        return "${date.dayOfMonth} $month ${date.year}"
    }

    /**
     * Formata timestamp para string legível
     */
    fun formatTimestamp(millis: Long): String {
        val dateTime = fromMillis(millis)
        return formatDate(dateTime.date)
    }

    /**
     * Retorna string de tempo relativo (ex: "há 2 horas", "ontem")
     */
    fun getRelativeTime(millis: Long): String {
        val now = now()
        val diff = now - millis
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            seconds < 60 -> "agora"
            minutes < 60 -> "há ${minutes}min"
            hours < 24 -> "há ${hours}h"
            days == 1L -> "ontem"
            days < 7 -> "há ${days} dias"
            days < 30 -> "há ${days / 7} semanas"
            days < 365 -> "há ${days / 30} meses"
            else -> "há ${days / 365} anos"
        }
    }

    /**
     * Converte LocalDate para timestamp (início do dia)
     */
    fun toMillis(date: LocalDate): Long {
        return date.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    /**
     * Obtém string de data no formato ISO (YYYY-MM-DD)
     */
    fun toIsoDate(date: LocalDate): String {
        return date.toString()
    }

    /**
     * Obtém string de data no formato ISO da data atual
     */
    fun todayIso(): String {
        return toIsoDate(today())
    }
}

/**
 * Extension: Formata LocalDate
 */
fun LocalDate.formatted(): String = DateTimeUtils.formatDate(this)

/**
 * Extension: Verifica se é hoje
 */
fun LocalDate.isToday(): Boolean = DateTimeUtils.isToday(this)

/**
 * Extension: Converte para millis
 */
fun LocalDate.toMillis(): Long = DateTimeUtils.toMillis(this)
