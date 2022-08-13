package ru.ac1d.tasktracker.common.helpers

import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppError
import ru.ac1d.tasktracker.common.models.TAppErrorLevels
import ru.ac1d.tasktracker.common.models.TAppStates

fun errorConcurrency(
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
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
