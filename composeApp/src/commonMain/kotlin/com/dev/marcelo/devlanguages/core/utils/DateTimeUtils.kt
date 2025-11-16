package com.dev.marcelo.devlanguages.core.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

/**
 * DateTime Utilities
 * Funções úteis para manipulação de datas e horas
 *
 * TODO: Implementar corretamente usando Clock.System (issue com kotlinx-datetime)
 */

@OptIn(kotlin.time.ExperimentalTime::class)
object DateTimeUtils {
    /**
     * Retorna o timestamp atual em milissegundos
     */
    fun now(): Long {
        // TODO: Fix Clock.System issue
        return 0L
    }

    /**
     * Retorna o Instant atual
     */
    fun nowInstant(): Instant {
        // TODO: Fix Clock.System issue
        return Instant.fromEpochMilliseconds(0)
    }

    /**
     * Retorna a data atual (sem hora)
     */
    fun today(): LocalDate {
        // TODO: Fix Clock.System issue
        return LocalDate(2025, 1, 1)
    }

    /**
     * Retorna a data/hora atual no timezone local
     */
    fun nowLocalDateTime(): LocalDateTime {
        // TODO: Fix Clock.System issue
        return LocalDateTime(2025, 1, 1, 0, 0)
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
     * Formata data/hora para string legível
     */
    fun formatDateTime(dateTime: LocalDateTime): String {
        val date = formatDate(dateTime.date)
        val hour = dateTime.hour.toString().padStart(2, '0')
        val minute = dateTime.minute.toString().padStart(2, '0')
        return "$date às $hour:$minute"
    }

    /**
     * Retorna string relativa ("hoje", "ontem", "3 dias atrás", etc)
     */
    fun getRelativeTime(date: LocalDate): String {
        val today = today()
        val daysDiff = (date.toEpochDays() - today.toEpochDays()).toLong()

        return when {
            daysDiff == 0L -> "Hoje"
            daysDiff == -1L -> "Ontem"
            daysDiff == 1L -> "Amanhã"
            daysDiff < 0L -> "${-daysDiff} dias atrás"
            else -> "Em $daysDiff dias"
        }
    }

    /**
     * Converte LocalDate para timestamp (millis)
     */
    fun toMillis(date: LocalDate): Long {
        return date.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    /**
     * Converte LocalDateTime para timestamp (millis)
     */
    fun toMillis(dateTime: LocalDateTime): Long {
        // Aproximação - converte para UTC
        return dateTime.date.toEpochDays() * 86400000L +
                dateTime.hour * 3600000L +
                dateTime.minute * 60000L +
                dateTime.second * 1000L
    }
}
