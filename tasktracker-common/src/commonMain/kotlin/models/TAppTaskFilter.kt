package ru.ac1d.tasktracker.common.models

data class TAppTaskFilter (
    var searchString: String = "",
    var reporterId: TAppUserId = TAppUserId.NONE,
    var executorId: TAppUserId = TAppUserId.NONE,
)
