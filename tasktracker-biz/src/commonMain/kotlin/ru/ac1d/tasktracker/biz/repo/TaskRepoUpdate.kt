package ru.ac1d.tasktracker.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppStates
import ru.ac1d.tasktracker.common.repo.DbTaskRequest

fun ICorChainDsl<TrackerAppContext>.taskRepoUpdate(title: String) = worker {
    this.title = title
    on { status == TAppStates.RUNNING }
    handle {
        val request = DbTaskRequest(
            taskRepoPrepare.deepCopy().apply {
                this.title = taskValidated.title
                description = taskValidated.description
                type = taskValidated.type
            }
        )
        val repoResponse = taskRepo.updateTask(request)
        val resultTask = repoResponse.result
        if (repoResponse.isSuccess && resultTask != null) {
            taskRepoDone = resultTask
        } else {
            status = TAppStates.FAILING
            errors.addAll(repoResponse.errors)
            taskRepoDone
        }
    }
}