package ru.ac1d.tasktracker.common.helpers

import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppError
import ru.ac1d.tasktracker.common.models.TAppStates

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
