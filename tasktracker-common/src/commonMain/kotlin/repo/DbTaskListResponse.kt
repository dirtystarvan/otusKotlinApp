package ru.ac1d.tasktracker.common.repo

import ru.ac1d.tasktracker.common.models.TAppError
import ru.ac1d.tasktracker.common.models.TAppTask

data class DbTaskListResponse(
    val result: List<TAppTask>?,
    val isSuccess: Boolean,
    val errors: List<TAppError> = emptyList()
)