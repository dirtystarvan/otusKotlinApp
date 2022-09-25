package ru.ac1d.tasktracker.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppStates
import ru.ac1d.tasktracker.common.repo.DbTaskFilterRequest

fun ICorChainDsl<TrackerAppContext>.taskRepoSearch(title: String) = worker {
    this.title = title
    on { status == TAppStates.RUNNING }
    handle {
        val request = DbTaskFilterRequest(
            titleFilter = taskFilterValidated.searchString,
            executorId = taskFilterValidated.executorId,
            reporterId = taskFilterValidated.reporterId,
            type = taskFilterValidated.taskType,
        )

        val repoResponse = taskRepo.searchTask(request)
        val resultTaskList = repoResponse.result

        if (repoResponse.isSuccess && resultTaskList != null) {
            taskListRepoDone = resultTaskList.toMutableList()
        } else {
            status = TAppStates.FAILING
            errors.addAll(repoResponse.errors)
        }
    }
}