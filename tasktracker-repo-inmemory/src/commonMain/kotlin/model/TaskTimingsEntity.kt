package model

import ru.ac1d.tasktracker.common.models.TAppTaskDate
import ru.ac1d.tasktracker.common.models.TAppTaskTimings

data class TaskTimingsEntity(
    val start: String? = null,
    val end: String? = null,
    val estimation: Float = 0.0f,
) {
    constructor(timings: TAppTaskTimings): this(
        start = timings.start.takeIf { it != TAppTaskDate.NONE }?.asString(),
        end = timings.end.takeIf { it != TAppTaskDate.NONE }?.asString(),
        estimation = timings.estimation
    )

    fun toInternal() = TAppTaskTimings(
        start = start?.let { TAppTaskDate(it) }?: TAppTaskDate.NONE,
        end = end?.let { TAppTaskDate(it) }?: TAppTaskDate.NONE,
        estimation = estimation
    )
}

internal fun TAppTaskTimings.toEntity() = TaskTimingsEntity(this)
