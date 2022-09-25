package ru.ac1d.tasktracker.common.models

import kotlinx.datetime.LocalDateTime
import ru.ac1d.tasktracker.common.NONE
import kotlin.jvm.JvmInline

@JvmInline
value class TAppTaskDate(private val date: String) {
    fun asString() = date

    fun asLocalDateTime() = LocalDateTime.parse(date)

    companion object {
        val NONE = TAppTaskDate(LocalDateTime.NONE.toString())
    }
}
