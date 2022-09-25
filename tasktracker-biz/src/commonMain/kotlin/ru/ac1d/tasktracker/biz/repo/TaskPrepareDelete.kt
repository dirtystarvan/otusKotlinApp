package ru.ac1d.tasktracker.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppStates

fun ICorChainDsl<TrackerAppContext>.taskRepoPrepareDelete(title: String) = worker {
    this.title = title
    on { status == TAppStates.RUNNING }
    handle {
        taskRepoPrepare = taskValidated.deepCopy()
    }
}