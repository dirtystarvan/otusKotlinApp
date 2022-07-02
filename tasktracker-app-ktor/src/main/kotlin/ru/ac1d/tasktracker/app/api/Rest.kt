package ru.ac1d.tasktracker.app.api

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Routing.api() {
    route("v1") {
        task()
    }
}

fun Route.task() {
    route("task") {
        post("create") {
            call.createTask()
        }
        post("read") {
            call.readTask()
        }
        post("update") {
            call.updateTask()
        }
        post("delete") {
            call.deleteTask()
        }
        post("search") {
            call.searchTask()
        }
    }
}
