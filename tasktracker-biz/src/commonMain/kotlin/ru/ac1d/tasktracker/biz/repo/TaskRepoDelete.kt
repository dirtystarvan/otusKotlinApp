package ru.ac1d.tasktracker.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppStates
import ru.ac1d.tasktracker.common.repo.DbTaskIdRequest

fun ICorChainDsl<TrackerAppContext>.taskRepoDelete(title: String) = worker {
    this.title = title
    on { status == TAppStates.RUNNING }
    handle {
        val request = DbTaskIdRequest(taskRepoPrepare)
        val repoResponse = taskRepo.deleteTask(request)
        if (! repoResponse.isSuccess) {
            status = TAppStates.FAILING
            errors.addAll(repoResponse.errors)
        }
        taskRepoDone = taskRepoRead
    }
}