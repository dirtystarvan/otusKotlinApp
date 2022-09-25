package ru.ac1d.tasktracker.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppStates
import ru.ac1d.tasktracker.common.repo.DbTaskIdRequest

fun ICorChainDsl<TrackerAppContext>.taskRepoRead(title: String) = worker {
    this.title = title
    on { status == TAppStates.RUNNING }
    handle {
        val request = DbTaskIdRequest(taskValidated)
        val repoResponse = taskRepo.readTask(request)
        val resultTask = repoResponse.result
        if (repoResponse.isSuccess && resultTask != null) {
            taskRepoRead = resultTask
        } else {
            status = TAppStates.FAILING
            errors.addAll(repoResponse.errors)
        }
    }
}


fun ICorChainDsl<TrackerAppContext>.taskRepoReadPrepareResponse(title: String) = worker {
    this.title = title
    on { status == TAppStates.RUNNING }
    handle { taskRepoDone = taskRepoRead }
}