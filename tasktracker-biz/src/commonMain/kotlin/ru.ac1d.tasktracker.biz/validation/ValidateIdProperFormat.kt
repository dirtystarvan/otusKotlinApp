package ru.ac1d.tasktracker.biz.validation

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.helpers.errorValidation
import ru.ac1d.tasktracker.common.helpers.fail
import ru.ac1d.tasktracker.common.models.TAppTaskId

fun ICorChainDsl<TrackerAppContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { taskValidating.id != TAppTaskId.NONE && ! taskValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = taskValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(errorValidation(
            field = "id",
            violationCode = "badFormat",
            description = "value $encodedId must contain only"
        ))
    }
}