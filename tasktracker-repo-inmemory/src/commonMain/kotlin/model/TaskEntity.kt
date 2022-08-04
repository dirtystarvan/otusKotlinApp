package model

import ru.ac1d.tasktracker.common.models.*

data class TaskEntity(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val ownerId: String? = null,
    val reporterId: String? = null,
    val executorId: String? = null,
    val status: String? = null,
    val type: String? = null,
    val timings: TaskTimingsEntity? = null,
    val subtasks: List<TaskEntity> = emptyList()
    //TODO: lock
) {
    constructor(task: TAppTask): this(
        id = task.id.asString().takeIf { it.isNotBlank() },
        title = task.title.takeIf { it.isNotBlank() },
        description = task.description.takeIf { it.isNotBlank() },
        ownerId = task.ownerId.asString().takeIf { it.isNotBlank() },
        reporterId = task.reporterId.asString().takeIf { it.isNotBlank() },
        executorId = task.executorId.asString().takeIf { it.isNotBlank() },
        status = task.status.takeIf { it != TAppTaskStatus.NONE }?.name,
        type = task.type.takeIf { it != TAppTaskType.NONE }?.name,
        timings = task.timings.toEntity(),
        subtasks = task.subtasks.toEntity(),
    )

    fun toInternal() = TAppTask(
        id = id?.let { TAppTaskId(it) }?: TAppTaskId.NONE,
        title = title ?: "",
        description = description ?: "",
        ownerId = ownerId?.let { TAppUserId(it) } ?: TAppUserId.NONE,
        reporterId = reporterId?.let { TAppUserId(it) } ?: TAppUserId.NONE,
        executorId = executorId?.let { TAppUserId(it) } ?: TAppUserId.NONE,
        status = status?.let { TAppTaskStatus.valueOf(it) } ?: TAppTaskStatus.NONE,
        type = type?.let { TAppTaskType.valueOf(it) } ?: TAppTaskType.NONE,
        timings = timings?.toInternal() ?: TAppTaskTimings(),
        subtasks = subtasks.toInternal()
    )
}

internal fun List<TaskEntity>.toInternal(): MutableList<TAppTask> {
    if (this.isEmpty()) {
        return mutableListOf()
    }

    return this.map { it.toInternal() }.toMutableList()
}

internal fun List<TAppTask>.toEntity() = this.map { task -> TaskEntity(task) }