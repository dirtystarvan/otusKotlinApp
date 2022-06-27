package ru.ac1d.tasktracker.common.models

import kotlinx.datetime.LocalDateTime
import kotlin.jvm.JvmInline

@JvmInline
value class TAppTaskDateImpl(private val date: String): TAppTaskDate {
    override fun asString() = date

    override fun asLocalDate() = LocalDateTime.parse(date)
}
