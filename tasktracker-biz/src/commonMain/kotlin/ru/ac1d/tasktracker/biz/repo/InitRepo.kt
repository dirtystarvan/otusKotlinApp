package ru.ac1d.tasktracker.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.helpers.errorAdministration
import ru.ac1d.tasktracker.common.helpers.fail
import ru.ac1d.tasktracker.common.models.TAppWorkMode
import ru.ac1d.tasktracker.common.repo.ITaskRepo

fun ICorChainDsl<TrackerAppContext>.initRepo(title: String) = worker {
    this.title = title
    handle {
        taskRepo = when (workMode) {
            TAppWorkMode.TEST -> settings.repoTest
            TAppWorkMode.STUB -> ITaskRepo.NONE
            else -> settings.repoProd
        }

        if (workMode != TAppWorkMode.STUB && taskRepo == ITaskRepo.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen workmode ($workMode). "
            )
        )
    }
}