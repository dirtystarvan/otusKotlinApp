package ru.ac1d.tasktracker.common.models

data class TAppTaskTimings (
    val start: TAppTaskDate = TAppTaskDate.NONE,
    val end: TAppTaskDate = TAppTaskDate.NONE,
    var estimation: Float = 0.0f
)
