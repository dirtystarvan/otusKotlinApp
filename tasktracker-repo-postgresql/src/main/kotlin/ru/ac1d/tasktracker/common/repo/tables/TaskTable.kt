package ru.ac1d.tasktracker.common.repo.tables

import org.jetbrains.exposed.sql.ResultRow
import ru.ac1d.tasktracker.common.models.*
import ru.ac1d.tasktracker.common.repo.tables.Timings.end
import ru.ac1d.tasktracker.common.repo.tables.Timings.estimation
import ru.ac1d.tasktracker.common.repo.tables.Timings.start


object Tasks: StringIdTable("tasks") {
    override val primaryKey = PrimaryKey(id)

    val title = varchar("title", 256)
    val description = varchar("description", 512)
    val ownerId = reference("owner_id", Owners)
    val reporterId = reference("reporter_id", Reporters)
    val executorId = reference("executor_id", Executors)
    val status = enumerationByName("status", 15, TAppTaskStatus::class)
    val type = enumerationByName("type", 15, TAppTaskType::class)
    val timings = reference("timings", Timings)
    val lock = varchar("lock", 100)

    fun from(result: ResultRow) = TAppTask(
        id = TAppTaskId(result[id].toString()),
        title = result[title],
        description = result[description],
        ownerId = TAppUserId(result[ownerId].toString()),
        reporterId = TAppUserId(result[reporterId].toString()),
        executorId = TAppUserId(result[executorId].toString()),
        status = result[status],
        type = result[type],
        timings = TAppTaskTimings(TAppTaskDate(result[start].toString()),
                                    TAppTaskDate(result[end].toString()),
                                    result[estimation]),
        lock = TAppTaskLock(result[lock])
    )
}
