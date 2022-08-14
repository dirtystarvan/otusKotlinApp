package ru.ac1d.tasktracker.common.helpers

import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppError
import ru.ac1d.tasktracker.common.models.TAppErrorLevels
import ru.ac1d.tasktracker.common.models.TAppStates

fun errorConcurrency(
    violationCode: String,
    description: String,
    level: TAppErrorLevels = TAppErrorLevels.ERROR,
) = TAppError(
    field = "lock",
    code = "concurrent-$violationCode",
    group = "concurrency",
    message = "Concurrent object access error: $description",
    level = level,
)

fun errorAdministration(
    field: String = "",
    violationCode: String,
    description: String,
    exception: Exception? = null,
    level: TAppErrorLevels = TAppErrorLevels.ERROR,
) = TAppError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    level = level,
)

fun Throwable.asTAppError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = TAppError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun TrackerAppContext.addError(error: TAppError) = errors.add(error)

fun TrackerAppContext.fail(error: TAppError) {
    addError(error)
    status = TAppStates.FAILING
}
