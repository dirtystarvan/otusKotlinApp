package ru.ac1d.tasktracker.common.models

data class TAppTask(
    var id: TAppTaskId = TAppTaskId.NONE,
    var title: String = "",
    var description: String = "",
    var type: TAppTaskType = TAppTaskType.NONE,
    var reporterId: TAppUserId = TAppUserId.NONE,
    var executorId: TAppUserId = TAppUserId.NONE,
    var status: TAppTaskStatus = TAppTaskStatus.NONE,
    val timings: TAppTaskTimings = TAppTaskTimings(),
    var subtasks: MutableList<TAppTask> = mutableListOf(),
    var ownerId: TAppUserId = TAppUserId.NONE,
    var lock: TAppTaskLock = TAppTaskLock.NONE,
    var permissionsClient: MutableSet<TAppTaskPermissionsClient> = mutableSetOf()
) {
    fun deepCopy() = TAppTask(
        id = this@TAppTask.id,
        title = this@TAppTask.title,
        description = this@TAppTask.description,
        type = this@TAppTask.type,
        reporterId = this@TAppTask.reporterId,
        executorId = this@TAppTask.executorId,
        status = this@TAppTask.status,
        timings = this@TAppTask.timings,
        subtasks = this@TAppTask.subtasks,
        ownerId = this@TAppTask.ownerId,
        lock = this@TAppTask.lock,
        permissionsClient = this@TAppTask.permissionsClient.toMutableSet()
    )
}
