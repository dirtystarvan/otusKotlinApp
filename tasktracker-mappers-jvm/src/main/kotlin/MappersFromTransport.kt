import exceptions.TAppUnknownRequestError
import ru.ac1d.api.v1.models.*
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.*
import ru.ac1d.tasktracker.common.stubs.TAppStubs

fun TrackerAppContext.fromTransport(request: IRequest) =
    when(request) {
        is TaskCreateRequest -> fromTransport(request)
        is TaskReadRequest -> fromTransport(request)
        is TaskUpdateRequest -> fromTransport(request)
        is TaskDeleteRequest -> fromTransport(request)
        is TaskSearchRequest -> fromTransport(request)
        else -> throw TAppUnknownRequestError(request.javaClass)
    }

fun TrackerAppContext.fromTransport(request: TaskCreateRequest) {
    command = TAppCommand.CREATE
    requestId = request.idFromTransport()
    taskRequest = request.task?.baseTask.fromTransport()
    workMode = request.debug.workModeFromTransport()
    stubCase = request.debug.stubCaseFromTransport()
}

fun TrackerAppContext.fromTransport(request: TaskReadRequest) {
    command = TAppCommand.READ
    requestId = request.idFromTransport()
    taskRequest = request.task.fromTransport()
    workMode = request.debug.workModeFromTransport()
    stubCase = request.debug.stubCaseFromTransport()
}

fun TrackerAppContext.fromTransport(request: TaskUpdateRequest) {
    command = TAppCommand.UPDATE
    requestId = request.idFromTransport()
    taskRequest = request.task?.baseTask.fromTransport()
    workMode = request.debug.workModeFromTransport()
    stubCase = request.debug.stubCaseFromTransport()
}

fun TrackerAppContext.fromTransport(request: TaskDeleteRequest) {
    command = TAppCommand.DELETE
    requestId = request.idFromTransport()
    taskRequest = request.task.fromTransport()
    workMode = request.debug.workModeFromTransport()
    stubCase = request.debug.stubCaseFromTransport()
}

fun TrackerAppContext.fromTransport(request: TaskSearchRequest) {
    command = TAppCommand.SEARCH
    requestId = request.idFromTransport()
    taskFilterRequest = request.taskFilter.fromTransport()
    workMode = request.debug.workModeFromTransport()
    stubCase = request.debug.stubCaseFromTransport()
}

private fun String?.toUserId() = this?.let { TAppUserId(it) } ?: TAppUserId.NONE
private fun String?.toTaskId() = this?.let { TAppTaskId(it) } ?: TAppTaskId.NONE

private fun IRequest?.idFromTransport() = this?.requestId?.let { TAppRequestId(it) } ?: TAppRequestId.NONE

private fun TaskWithIdRequestTask?.fromTransport() = this?.let {TAppTask(id = it.id.toTaskId())} ?: TAppTask()

private fun TaskSearchFilter?.fromTransport() = this?.let { TAppTaskFilter(searchString = it.searchString ?: "") } ?: TAppTaskFilter()

private fun TaskType?.fromTransport() =
    when(this) {
        TaskType.TASK -> TAppTaskType.TASK
        TaskType.STORY -> TAppTaskType.STORY
        TaskType.SUBTASK -> TAppTaskType.SUBTASK
        null -> TAppTaskType.NONE
    }

private fun TaskStatus?.fromTransport() =
    when(this) {
        TaskStatus.OPEN -> TAppTaskStatus.OPEN
        TaskStatus.IN_PROGRESS -> TAppTaskStatus.IN_PROGRESS
        TaskStatus.QA -> TAppTaskStatus.QA
        TaskStatus.RESOLVED -> TAppTaskStatus.RESOLVED
        TaskStatus.CLOSED -> TAppTaskStatus.CLOSED
        null -> TAppTaskStatus.NONE
    }

private fun TaskTimings?.fromTransport() : TAppTaskTimings {
    if (this == null)
        return TAppTaskTimings()

    return TAppTaskTimings(
        start = this.start?.let { TAppTaskDateImpl(it) } ?: TAppTaskDate.NONE,
        end = this.end?.let { TAppTaskDateImpl(it) } ?: TAppTaskDate.NONE,
        estimation = this.estimation ?: 0.0f
    )
}

private fun List<TaskUpdatable>?.fromTransport() : MutableList<TAppTask> {
    if (this == null || this.isEmpty())
        return mutableListOf()

    return this.map { it.let { it.fromTransport()} }.toMutableList()
}

private fun TaskUpdatable?.fromTransport() : TAppTask {
    if (this == null)
        return TAppTask()

    return TAppTask(
        title = this.title ?: "",
        description = this.description ?: "",
        type = this.type.fromTransport(),
        reporterId = this.reporterId.toUserId(),
        executorId = this.reporterId.toUserId(),
        status = this.status.fromTransport(),
        timings = this.timings.fromTransport(),
        subtasks = this.subtasks.fromTransport(),
    )
}

private fun TaskDebug?.workModeFromTransport() =
    when (this?.mode) {
        TaskRequestDebugMode.PROD -> TAppWorkMode.PROD
        TaskRequestDebugMode.TEST -> TAppWorkMode.TEST
        TaskRequestDebugMode.STUB -> TAppWorkMode.STUB
        null -> TAppWorkMode.PROD
    }

private fun TaskDebug?.stubCaseFromTransport() =
    when(this?.stub) {
        TaskRequestDebugStubs.SUCCESS -> TAppStubs.SUCCESS
        TaskRequestDebugStubs.NOT_FOUND -> TAppStubs.NOT_FOUND
        TaskRequestDebugStubs.BAD_ID -> TAppStubs.BAD_ID
        TaskRequestDebugStubs.BAD_TITLE -> TAppStubs.BAD_TITLE
        TaskRequestDebugStubs.BAD_DESCRIPTION -> TAppStubs.BAD_DESCRIPTION
        TaskRequestDebugStubs.CANNOT_DELETE -> TAppStubs.CANNOT_DELETE
        TaskRequestDebugStubs.BAD_SEARCH_STRING -> TAppStubs.BAD_SEARCH_STRING
        null -> TAppStubs.NONE
    }
