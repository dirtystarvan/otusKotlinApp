package ru.ac1d.tasktracker.biz.validation

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.helpers.errorValidation
import ru.ac1d.tasktracker.common.helpers.fail

fun ICorChainDsl<TrackerAppContext>.validateDescriptionHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { taskValidating.description.isNotEmpty() && ! taskValidating.description.contains(regExp) }
    handle {
        fail(errorValidation(
            field = "description",
            violationCode = "noContent",
            description = "field must contain letters"
        ))
    }
}
