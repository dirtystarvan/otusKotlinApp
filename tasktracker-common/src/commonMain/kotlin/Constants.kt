package ru.ac1d.tasktracker.common

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private val INSTANT_NONE = Instant.fromEpochMilliseconds(Long.MIN_VALUE)

val Instant.Companion.NONE
    get() = INSTANT_NONE

val LocalDateTime.Companion.NONE
    get() = Instant.NONE.toLocalDateTime(TimeZone.currentSystemDefault())