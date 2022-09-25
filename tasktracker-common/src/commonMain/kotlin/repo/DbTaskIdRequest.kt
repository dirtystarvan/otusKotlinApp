package ru.ac1d.tasktracker.common.repo

import ru.ac1d.tasktracker.common.models.TAppTask
import ru.ac1d.tasktracker.common.models.TAppTaskId
import ru.ac1d.tasktracker.common.models.TAppTaskLock

data class DbTaskIdRequest(
    val id: TAppTaskId,
    val lock: TAppTaskLock = TAppTaskLock.NONE,
) {
    constructor(task: TAppTask) : this(id = task.id, lock = task.lock)
}