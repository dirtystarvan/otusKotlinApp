package ru.ac1d.tasktracker.app.api

import ru.ac1d.api.v1.models.ResponseResult
import ru.ac1d.tasktracker.common.models.TAppError

fun buildError() = TAppError(
    field = "_", code = ResponseResult.ERROR.value
)