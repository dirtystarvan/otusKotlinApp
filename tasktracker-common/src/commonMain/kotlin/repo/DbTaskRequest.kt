package ru.ac1d.tasktracker.common.repo

import ru.ac1d.tasktracker.common.models.TAppTask

data class DbTaskRequest(
    val task: TAppTask
)