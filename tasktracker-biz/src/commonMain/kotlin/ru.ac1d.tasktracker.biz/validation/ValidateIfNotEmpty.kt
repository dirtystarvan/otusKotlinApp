package ru.ac1d.tasktracker.biz.validation

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.helpers.errorValidation
import ru.ac1d.tasktracker.common.helpers.fail

fun ICorChainDsl<TrackerAppContext>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { taskValidating.id.asString().isEmpty() }
    handle {
        fail(errorValidation(
            field = "id",
            violationCode = "empty",
            description = "field must not be empty"
        ))
    }
}
