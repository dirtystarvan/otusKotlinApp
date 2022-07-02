package ru.ac1d.tasktracker.app.api

import TaskService
import fromTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.koin.ktor.ext.get
import ru.ac1d.api.v1.models.*
import ru.ac1d.tasktracker.common.TrackerAppContext
import toTransportCreate
import toTransportDelete
import toTransportRead
import toTransportSearch
import toTransportUpdate


val ApplicationCall.taskService: TaskService
    get() = get<TaskService>()

suspend fun ApplicationCall.createTask() {
    val createTaskRequest = receive<TaskCreateRequest>()

    respond(
        TrackerAppContext().apply { fromTransport(createTaskRequest) }.let(taskService::createTask).toTransportCreate()
    )
}

suspend fun ApplicationCall.readTask() {
    val readTaskRequest = receive<TaskReadRequest>()

    respond(
        TrackerAppContext().apply { fromTransport(readTaskRequest) }.let { taskService.readTask(it, ::buildError) }.toTransportRead()
    )
}

suspend fun ApplicationCall.updateTask() {
    val updateTaskRequest = receive<TaskUpdateRequest>()

    respond(
        TrackerAppContext().apply { fromTransport(updateTaskRequest) }.let {
            taskService.updateTask(it, ::buildError)
        }.toTransportUpdate()
    )
}

suspend fun ApplicationCall.deleteTask() {
    val deleteTaskRequest = receive<TaskDeleteRequest>()

    respond(
        TrackerAppContext().apply { fromTransport(deleteTaskRequest) }.let {
            taskService.deleteTask(it, ::buildError)
        }.toTransportDelete()
    )
}

suspend fun ApplicationCall.searchTask() {
    val searchTaskRequest = receive<TaskSearchRequest>()

    respond(
        TrackerAppContext().apply { fromTransport(searchTaskRequest) }.let {
            taskService.searchTask(it, ::buildError)
        }.toTransportSearch()
    )
}
