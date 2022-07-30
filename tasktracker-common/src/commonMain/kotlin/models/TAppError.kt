package ru.ac1d.tasktracker.common.models

data class TAppError (
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)
