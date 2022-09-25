package ru.ac1d.tasktracker.biz.validation

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.helpers.errorValidation
import ru.ac1d.tasktracker.common.helpers.fail

fun ICorChainDsl<TrackerAppContext>.validateTitleHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { taskValidating.title.isNotEmpty() && ! taskValidating.title.contains(regExp) }
    handle {
        fail(errorValidation(
            field = "title",
            violationCode = "noContent",
            description = "field must contain letters"
        ))
    }
}
