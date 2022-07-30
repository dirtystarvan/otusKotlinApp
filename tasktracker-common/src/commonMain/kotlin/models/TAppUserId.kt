package ru.ac1d.tasktracker.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class TAppUserId(private val id: String) {
    fun asString(): String = id

    companion object {
        val NONE = TAppUserId("")
    }
}
