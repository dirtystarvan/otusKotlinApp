package ru.ac1d.tasktracker.common.helpers

import ru.ac1d.tasktracker.common.models.TAppError
import ru.ac1d.tasktracker.common.models.TAppErrorLevels

fun errorValidation(
    field: String,
    violationCode: String,
    description: String,
    level: TAppErrorLevels = TAppErrorLevels.ERROR,
) = TAppError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)
