package ru.ac1d.tasktracker.biz.stubs

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppError
import ru.ac1d.tasktracker.common.models.TAppStates
import ru.ac1d.tasktracker.common.stubs.TAppStubs

fun ICorChainDsl<TrackerAppContext>.stubValidationBadTitle(title: String) = worker {
    this.title = title
    on { stubCase == TAppStubs.BAD_TITLE && status == TAppStates.RUNNING }
    handle {
        status = TAppStates.FAILING
        this.errors.add(
            TAppError(
                group = "validation",
                code = "validation-title",
                field = "title",
                message = "Title validation error"
            )
        )
    }
}

fun ICorChainDsl<TrackerAppContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    on { stubCase == TAppStubs.BAD_ID && status == TAppStates.RUNNING }
    handle {
        status = TAppStates.FAILING
        this.errors.add(
            TAppError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Bad id"
            )
        )
    }
}

fun ICorChainDsl<TrackerAppContext>.stubValidationBadDescription(title: String) = worker {
    this.title = title
    on { stubCase == TAppStubs.BAD_DESCRIPTION && status == TAppStates.RUNNING }
    handle {
        status = TAppStates.FAILING
        this.errors.add(
            TAppError(
                group = "validation",
                code = "validation-description",
                field = "description",
                message = "Description validation error"
            )
        )
    }
}
