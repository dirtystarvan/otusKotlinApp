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
    var permissionsClient: MutableSet<TAppTaskPermissionsClient> = mutableSetOf()
)
