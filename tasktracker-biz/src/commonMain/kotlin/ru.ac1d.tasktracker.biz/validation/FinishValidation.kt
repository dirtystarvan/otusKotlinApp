package ru.ac1d.tasktracker.biz.validation

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppStates

fun ICorChainDsl<TrackerAppContext>.finishAdValidation(title: String) = worker {
    this.title = title
    on { status == TAppStates.RUNNING }
    handle {
        taskValidated = taskValidating
    }
}

fun ICorChainDsl<TrackerAppContext>.finishAdFilterValidation(title: String) = worker {
    this.title = title
    on { status == TAppStates.RUNNING }
    handle {
        taskFilterValidated = taskFilterValidating
    }
}
