package ru.ac1d.tasktracker.biz.general

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppStates
import ru.ac1d.tasktracker.common.models.TAppWorkMode

fun ICorChainDsl<TrackerAppContext>.prepareResult(title: String) = worker {
    this.title = title
    on { workMode != TAppWorkMode.STUB }
    handle {
        taskResponse = taskRepoDone
        taskListResponse = taskListRepoDone
        status = when (val st = status) {
            TAppStates.RUNNING -> TAppStates.FINISHING
            else -> st
        }
    }
}