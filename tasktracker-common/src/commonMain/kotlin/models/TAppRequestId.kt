package ru.ac1d.tasktracker.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class TAppRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = TAppRequestId("")
    }
}