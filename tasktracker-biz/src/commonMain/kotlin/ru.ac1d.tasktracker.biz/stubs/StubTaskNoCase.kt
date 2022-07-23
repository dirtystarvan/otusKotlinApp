package ru.ac1d.tasktracker.biz.stubs

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppError
import ru.ac1d.tasktracker.common.models.TAppStates

fun ICorChainDsl<TrackerAppContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { status == TAppStates.RUNNING }
    handle {
        status = TAppStates.FAILING
        this.errors.add(
            TAppError(
                group = "validation",
                code = "validation-nocase",
                field = "stub",
                message = "Wrong stub case: ${stubCase.name}"
            )
        )
    }
}
