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
    val ownerId = reference("ownerId", Users)
    val reporterId = reference("reporter_id", Users)
    val executorId = reference("executor_id", Users)
    val status = enumerationByName("status", 15, TAppTaskStatus::class)
    val type = enumerationByName("type", 15, TAppTaskType::class)
    val timings = reference("timings", Timings)
//    val parent = reference("parent_task", Tasks)
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
        timings = TAppTaskTimings(TAppTaskDateImpl(result[start].toString()),
                                    TAppTaskDateImpl(result[end].toString()),
                                    result[estimation]),
        lock = TAppTaskLock(result[lock])
    )
}
