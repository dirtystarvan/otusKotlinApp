package ru.ac1d.tasktracker.common.repo

import ru.ac1d.tasktracker.common.models.TAppTask
import ru.ac1d.tasktracker.common.models.TAppTaskId

data class DbTaskIdRequest(
    val id: TAppTaskId
    //TODO lock
) {
    constructor(task: TAppTask) : this(id = task.id)
}