package ru.ac1d.tasktracker.common.repo

import com.benasher44.uuid.uuid4
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.ac1d.tasktracker.common.helpers.errorConcurrency
import ru.ac1d.tasktracker.common.models.*
import ru.ac1d.tasktracker.common.repo.tables.Tasks
import ru.ac1d.tasktracker.common.repo.tables.Tasks.timings
import ru.ac1d.tasktracker.common.repo.tables.Timings
import ru.ac1d.tasktracker.common.repo.tables.Users
import java.sql.SQLException
import java.util.*

class TaskRepoPostgres(
    initObjects: Collection<TAppTask> = emptyList()
): ITaskRepo {
    private val db by lazy { DbConnector().connect(Tasks, Users, Timings) }
    private val mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    override suspend fun createTask(request: DbTaskRequest): DbTaskResponse {
        val task = request.task.copy(lock = TAppTaskLock(uuid4().toString()))
        return mutex.withLock {
            save(task)
        }
    }

    override suspend fun readTask(request: DbTaskIdRequest): DbTaskResponse {
        return safeTransaction({
            val result = (Tasks innerJoin Users innerJoin Timings).select { Tasks.id.eq(request.id.asString()) }.single()

            DbTaskResponse(Tasks.from(result), true)
        }, {
            val err = when (this) {
                is NoSuchElementException -> TAppError(field = "id", message = "Not Found")
                is IllegalArgumentException -> TAppError(message = "More than one element with the same id")
                else -> TAppError(message = localizedMessage)
            }
            DbTaskResponse(result = null, isSuccess = false, errors = listOf(err))
        })
    }

    override suspend fun updateTask(request: DbTaskRequest): DbTaskResponse {
        val key = request.task.id.takeIf { it != TAppTaskId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = request.task.lock.takeIf { it != TAppTaskLock.NONE }?.asString()
        val newTask = request.task.copy(lock = TAppTaskLock(UUID.randomUUID().toString()))

        return mutex.withLock {
            safeTransaction({
                val local = Tasks.select { Tasks.id.eq(key) }.singleOrNull()?.let {
                    Tasks.from(it)
                } ?: return@safeTransaction resultErrorNotFound

                return@safeTransaction when (oldLock) {
                    null, local.lock.asString() -> updateDb(newTask)
                    else -> resultErrorConcurrent
                }
            }, {
                DbTaskResponse(
                    result = null,
                    isSuccess = false,
                    errors = listOf(TAppError(field = "id", message = "Not Found"))
                )
            })
        }
    }

    override suspend fun deleteTask(request: DbTaskIdRequest): DbTaskResponse {
        val key = request.id.takeIf { it != TAppTaskId.NONE }?.asString() ?: return resultErrorEmptyId

        return mutex.withLock {
            safeTransaction({
                val local = Tasks.select { Tasks.id.eq(key) }.single().let { Tasks.from(it) }

                if (local.lock == request.lock) {
                    Tasks.deleteWhere { Tasks.id eq request.id.asString() }
                    DbTaskResponse(result = local, isSuccess = true)
                } else {
                    resultErrorConcurrent
                }
            }, {
                DbTaskResponse(
                    result = null,
                    isSuccess = false,
                    errors = listOf(TAppError(field = "id", message = "Not Found"))
                )
            })
        }
    }

    override suspend fun searchTask(request: DbTaskFilterRequest): DbTaskListResponse {
        return safeTransaction({
            val results = (Tasks innerJoin Users innerJoin Timings).select {
                (if (request.reporterId == TAppUserId.NONE) Op.TRUE else Tasks.reporterId eq request.reporterId.asString()) and
                (if (request.executorId == TAppUserId.NONE) Op.TRUE else Tasks.executorId eq request.executorId.asString()) and
                (if (request.titleFilter.isBlank()) Op.TRUE else (Tasks.title like "%${request.titleFilter}%") or
                        (Tasks.description like "%${request.titleFilter}%")
                ) and
                (if (request.type == TAppTaskType.NONE) Op.TRUE else Tasks.type eq request.type)
            }

            DbTaskListResponse(result = results.map { Tasks.from(it) }, isSuccess = true)
        }, {
            DbTaskListResponse(result = emptyList(), isSuccess = false, listOf(TAppError(message = localizedMessage)))
        })
    }

    private fun save(task: TAppTask): DbTaskResponse {
        return safeTransaction({
            val ownerIdKey = Users.insertIgnore {
                if (task.ownerId != TAppUserId.NONE) {
                    it[id] = task.ownerId.asString()
                }
            } get Users.id

            val executorIdKey = Users.insertIgnore {
                if (task.executorId != TAppUserId.NONE) {
                    it[id] = task.executorId.asString()
                }
            } get Users.id

            val reporterIdKey = Users.insertIgnore {
                if (task.reporterId != TAppUserId.NONE) {
                    it[id] = task.reporterId.asString()
                }
            } get Users.id

            val timingsKey = Timings.insertIgnore {
                if (task.timings.start != TAppTaskDate.NONE) {
                    it[start] = task.timings.start.asLocalDate()
                }
            } get Timings.id

            val res = Tasks.insert {
                if (task.id != TAppTaskId.NONE) {
                    it[id] = task.id.asString()
                }
                it[title] = task.title
                it[description] = task.description
                it[ownerId] = ownerIdKey
                it[executorId] = executorIdKey
                it[reporterId] = reporterIdKey
                it[type] = task.type
                it[timings] =
//                it[lock] = task.lock.asString()
            }
//            val title = Tasks.varchar("title", 256)
//            val description = Tasks.varchar("description", 512)
//            val ownerId = Tasks.reference("ownerId", Users)
//            val reporterId = Tasks.reference("reporter_id", Users)
//            val executorId = Tasks.reference("executor_id", Users)
//            val status = Tasks.enumerationByName("status", 15, TAppTaskStatus::class)
//            val type = Tasks.enumerationByName("type", 15, TAppTaskType::class)
//            val timings = Tasks.reference("timings", Timings)
//            val parent = Tasks.reference("parent_task", Tasks)
//            val lock = Tasks.varchar("lock", 100)
            DbTaskResponse(Tasks.from(res), true)//TODO: from mappers
        }, {
            DbTaskResponse(
                result = null,
                isSuccess = false,
                errors = listOf(TAppError(message = message ?: localizedMessage))
            )
        })
    }

    private fun <T> safeTransaction(statement: Transaction.() -> T, handleException: Throwable.() -> T): T {
        return try {
            transaction(db, statement)
        } catch (e: SQLException) {
            throw e
        } catch (e: Throwable) {
            return handleException(e)
        }
    }

    private fun updateDb(task: TAppTask): DbTaskResponse {
        Users.insertIgnore {
            if (task.ownerId != TAppUserId.NONE) {
                it[id] = task.ownerId.asString()
            }
        }

        Tasks.update({ Tasks.id.eq(task.id.asString()) }) {
            it[title] = task.title
            it[description] = task.description
            it[ownerId] = task.ownerId.asString()
            it[type] = task.type
            //TODO: another fields
        }
        val result = Tasks.select { Tasks.id.eq(task.id.asString()) }.single()

        return DbTaskResponse(result = Tasks.from(result), isSuccess = true)
    }


    companion object {
        val resultErrorEmptyId = DbTaskResponse(
            result = null,
            isSuccess = false,
            errors = listOf(
                TAppError(
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )
        val resultErrorConcurrent = DbTaskResponse(
            result = null,
            isSuccess = false,
            errors = listOf(
                errorConcurrency(
                    violationCode = "changed",
                    description = "Object has changed during request handling"
                ),
            )
        )
        val resultErrorNotFound = DbTaskResponse(
            isSuccess = false,
            result = null,
            errors = listOf(
                TAppError(
                    field = "id",
                    message = "Not Found"
                )
            )
        )
    }
}
