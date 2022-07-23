package ru.ac1d.tasktracker.common

import ru.ac1d.tasktracker.common.models.*
import ru.ac1d.tasktracker.common.stubs.TAppStubs

data class TrackerAppContext (
    var command: TAppCommand = TAppCommand.NONE,
    var status: TAppStates = TAppStates.NONE,
    val errors: MutableList<TAppError> = mutableListOf(),

    var workMode: TAppWorkMode = TAppWorkMode.PROD,
    var stubCase: TAppStubs = TAppStubs.NONE,

    var requestId: TAppRequestId = TAppRequestId.NONE,
    var taskRequest: TAppTask = TAppTask(),
    var taskFilterRequest: TAppTaskFilter = TAppTaskFilter(),
    var taskResponse: TAppTask = TAppTask(),
    var taskListResponse: MutableList<TAppTask> = mutableListOf(),

    var taskValidating: TAppTask = TAppTask(),
    var taskFilterValidating: TAppTaskFilter = TAppTaskFilter(),

    var taskValidated: TAppTask = TAppTask(),
    var taskFilterValidated: TAppTaskFilter = TAppTaskFilter(),
)