package ru.ac1d.tasktracker.biz.stubs

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppError
import ru.ac1d.tasktracker.common.models.TAppStates
import ru.ac1d.tasktracker.common.stubs.TAppStubs

fun ICorChainDsl<TrackerAppContext>.stubDbError(title: String) = worker {
    this.title = title
    on { stubCase == TAppStubs.DB_ERROR && status == TAppStates.RUNNING }
    handle {
        status = TAppStates.FAILING
        this.errors.add(
            TAppError(
                group = "internal",
                code = "db",
                message = "Database error"
            )
        )
    }
}
