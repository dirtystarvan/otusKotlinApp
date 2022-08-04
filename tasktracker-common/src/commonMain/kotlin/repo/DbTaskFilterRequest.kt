package ru.ac1d.tasktracker.common.repo

import ru.ac1d.tasktracker.common.models.TAppTaskType
import ru.ac1d.tasktracker.common.models.TAppUserId

data class DbTaskFilterRequest(
    val titleFilter: String = "",
    val ownerId: TAppUserId = TAppUserId.NONE,
    val type: TAppTaskType = TAppTaskType.NONE,
)