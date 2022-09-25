package ru.ac1d.tasktracker.app.koins

import TaskService
import org.koin.dsl.module

val taskServiceKoin = module {
    single { TaskService() }
}
