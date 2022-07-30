package ru.ac1d.tasktracker.common.models

import kotlinx.datetime.LocalDateTime
import ru.ac1d.tasktracker.common.NONE

interface TAppTaskDate {
    fun asString() : String
    fun asLocalDate() : LocalDateTime

    companion object {
        val NONE = object: TAppTaskDate {
            val date: String = ""

            override fun asString() = date
            override fun asLocalDate() = LocalDateTime.NONE
        }
    }
}
