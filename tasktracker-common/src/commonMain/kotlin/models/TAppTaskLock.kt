package ru.ac1d.tasktracker.common.models

@JvmInline
value class TAppTaskLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = TAppTaskLock("")
    }
}
