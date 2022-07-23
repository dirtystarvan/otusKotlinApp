package ru.ac1d.tasktracker.biz.validation

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.helpers.errorValidation
import ru.ac1d.tasktracker.common.helpers.fail

fun ICorChainDsl<TrackerAppContext>.validateDescriptionNotEmpty(title: String) = worker {
    this.title = title
    on { taskValidating.description.isEmpty() }
    handle {
        fail(errorValidation(
            field = "description",
            violationCode = "empty",
            description = "field must not be empty"
        ))
    }
}