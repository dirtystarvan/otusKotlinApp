package ru.ac1d.tasktracker.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.helpers.errorConcurrency
import ru.ac1d.tasktracker.common.helpers.fail
import ru.ac1d.tasktracker.common.models.TAppStates

fun ICorChainDsl<TrackerAppContext>.repoCheckLock(title: String) = worker {
    this.title = title
    on { status == TAppStates.RUNNING && taskValidated.lock != taskRepoRead.lock }
    handle {
        fail(errorConcurrency(violationCode = "changed", "Object has been inconsistently changed"))
        taskRepoDone = taskRepoRead
    }
}