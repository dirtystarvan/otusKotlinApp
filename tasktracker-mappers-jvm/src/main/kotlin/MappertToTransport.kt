import exceptions.TAppUnknownCommandError
import ru.ac1d.api.v1.models.*
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.*

fun TrackerAppContext.toTransport(): IResponse =
    when (command) {
        TAppCommand.CREATE -> toTransportCreate()
        TAppCommand.READ -> toTransportRead()
        TAppCommand.UPDATE -> toTransportUpdate()
        TAppCommand.DELETE -> toTransportDelete()
        TAppCommand.SEARCH -> toTransportSearch()
        TAppCommand.NONE -> throw TAppUnknownCommandError(command)
    }

fun TrackerAppContext.toTransportCreate() = TaskCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (status == TAppStates.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransport(),
)

fun TrackerAppContext.toTransportRead() = TaskReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (status == TAppStates.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransport(),
)

fun TrackerAppContext.toTransportUpdate() = TaskUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (status == TAppStates.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransport(),
)

fun TrackerAppContext.toTransportDelete() = TaskDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (status == TAppStates.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransport(),
)

fun TrackerAppContext.toTransportSearch() = TaskSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (status == TAppStates.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    tasks = taskListResponse.transportTasksResponse()
)

private fun List<TAppError>?.toTransportErrors() : List<Error>? = this?.map { it.toTransport() }.takeIf { it?.isNotEmpty() ?: false }

private fun TAppTask.toTransport() = TaskResponseObject(
    id = id.takeIf { it != TAppTaskId.NONE }?.asString(),
    baseTask = baseTaskObject(),
    ownerId = ownerId.takeIf { it != TAppUserId.NONE }?.asString(),
    permissions = permissionsClient.toTransportTaskPerm(),
    lock = lock.asString()
)

private fun TAppTask.baseTaskObject() = TaskUpdatable(
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    type = type.toTransport(),
    reporterId = reporterId.takeIf { it != TAppUserId.NONE }?.asString(),
    executorId = executorId.takeIf { it != TAppUserId.NONE }?.asString(),
    status = status.toTransport(),
    timings = timings.toTransport(),
    subtasks = subtasks.toTransportTasks(),
)

private fun List<TAppTask>.toTransportTasks() : List<TaskUpdatable>? =
    this.map { it.baseTaskObject() }
        .takeIf { it.isNotEmpty() }

private fun List<TAppTask>.transportTasksResponse() : List<TaskResponseObject>? =
    this.map { it.toTransport() }.takeIf { it.isNotEmpty() }

private fun TAppError.toTransport() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() }
)

private fun Set<TAppTaskPermissionsClient>.toTransportTaskPerm(): Set<TaskPermissions>? =
    this.map { it.toTransport() }
        .toSet()
        .takeIf { it.isNotEmpty() }

private fun TAppTaskPermissionsClient.toTransport() =
    when(this) {
        TAppTaskPermissionsClient.READ -> TaskPermissions.READ
        TAppTaskPermissionsClient.UPDATE -> TaskPermissions.UPDATE
        TAppTaskPermissionsClient.DELETE -> TaskPermissions.DELETE
    }

private fun TAppTaskType.toTransport() =
    when(this) {
        TAppTaskType.TASK -> TaskType.TASK
        TAppTaskType.STORY -> TaskType.STORY
        TAppTaskType.SUBTASK -> TaskType.SUBTASK
        TAppTaskType.NONE -> null
    }

private fun TAppTaskStatus.toTransport() =
    when(this) {
        TAppTaskStatus.OPEN -> TaskStatus.OPEN
        TAppTaskStatus.IN_PROGRESS -> TaskStatus.IN_PROGRESS
        TAppTaskStatus.QA -> TaskStatus.QA
        TAppTaskStatus.RESOLVED -> TaskStatus.RESOLVED
        TAppTaskStatus.CLOSED -> TaskStatus.CLOSED
        TAppTaskStatus.NONE -> null
    }

private fun TAppTaskTimings.toTransport() = TaskTimings(
    start = start.takeIf { it != TAppTaskDate.NONE }?.asString(),
    end = end.takeIf { it != TAppTaskDate.NONE }?.asString(),
    estimation = estimation
)
