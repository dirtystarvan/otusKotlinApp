package ru.ac1d.tasktracker.common.repo

import com.benasher44.uuid.uuid4
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import ru.ac1d.tasktracker.common.helpers.errorConcurrency
import ru.ac1d.tasktracker.common.models.*
import ru.ac1d.tasktracker.common.repo.tables.*
import java.sql.SQLException
import java.util.*

class TaskRepoPostgres(
    url: String = "jdbc:postgresql://localhost:5432/tasker_db",
    user: String = "postgres",
    password: String = "strongpassword",
    schema: String = "tasker_db",
    initObjects: Collection<TAppTask> = emptyList()
): ITaskRepo {
    private val db by lazy { DbConnector(url, user, password, schema)
        .connect(Tasks, Owners, Executors, Reporters, Timings) }
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
            val result = (Tasks innerJoin Owners innerJoin Reporters innerJoin Executors innerJoin Timings)
                .select { Tasks.id.eq(request.id.asString()) }.single()

            DbTaskResponse(Tasks.from(result), true)
        }, {
            val err = when (this) {
                is NoSuchElementException -> TAppError(field = "id", message = "Not found")
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
                val local = (Tasks innerJoin Owners innerJoin Reporters innerJoin Executors innerJoin Timings)
                    .select { Tasks.id.eq(key) }.singleOrNull()
                    ?.let { Tasks.from(it) }
                    ?: return@safeTransaction resultErrorNotFound

                return@safeTransaction when (oldLock) {
                    null, local.lock.asString() -> updateDb(newTask)
                    else -> resultErrorConcurrent
                }
            }, {
                val errors = listOf(TAppError(field = "id", message = "Not found"), TAppError(message = this.localizedMessage))
                DbTaskResponse(result = null, isSuccess = false, errors = errors)
            })
        }
    }

    override suspend fun deleteTask(request: DbTaskIdRequest): DbTaskResponse {
        val key = request.id.takeIf { it != TAppTaskId.NONE }?.asString() ?: return resultErrorEmptyId

        return mutex.withLock {
            safeTransaction({
                val local = (Tasks innerJoin Owners innerJoin Reporters innerJoin Executors innerJoin Timings)
                    .select { Tasks.id.eq(key) }.single().let { Tasks.from(it) }

                if (local.lock == request.lock) {
                    Tasks.deleteWhere { Tasks.id eq request.id.asString() }
                    DbTaskResponse(result = local, isSuccess = true)
                } else {
                    resultErrorConcurrent
                }
            }, {
                resultErrorNotFound
            })
        }
    }

    override suspend fun searchTask(request: DbTaskFilterRequest): DbTaskListResponse {
        return safeTransaction({
            val results = (Tasks innerJoin Owners innerJoin Reporters innerJoin Executors innerJoin Timings)
                .select {
                (if (request.ownerId == TAppUserId.NONE) Op.TRUE else Tasks.ownerId eq request.ownerId.asString()) and
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

    private fun <T> safeTransaction(statement: Transaction.() -> T, handleException: Throwable.() -> T): T {
        return try {
            transaction(db, statement)
        } catch (e: SQLException) {
            throw e
        } catch (e: Throwable) {
            return handleException(e)
        }
    }

    private fun updateForeignOwner(task: TAppTask): EntityID<String> {
        val ownerIdKey = Owners.insertIgnore {
            if (task.ownerId != TAppUserId.NONE) {
                it[id] = task.ownerId.asString()
            }
        } get Owners.id

        return ownerIdKey
    }

    private fun updateForeignExecutor(task: TAppTask): EntityID<String> {
        val executorIdKey = Executors.insertIgnore {
            if (task.executorId != TAppUserId.NONE) {
                it[id] = task.executorId.asString()
            }
        } get Executors.id

        return executorIdKey
    }

    private fun updateForeignReporter(task: TAppTask): EntityID<String> {
        val reporterIdKey = Reporters.insertIgnore {
            if (task.reporterId != TAppUserId.NONE) {
                it[id] = task.reporterId.asString()
            }
        } get Reporters.id

        return reporterIdKey
    }

    private fun updateForeignTimings(task: TAppTask): EntityID<Int> {
        val timingsIdKey = Timings.insertIgnore {
            it[start] = task.timings.start.asString()
            it[end] = task.timings.end.asString()
            it[estimation] = task.timings.estimation
        } get Timings.id

        return timingsIdKey
    }

    private fun fillTasksUpdateBuilder(statement: UpdateBuilder<*>, task: TAppTask) {
        statement[Tasks.title] = task.title
        statement[Tasks.description] = task.description
        statement[Tasks.ownerId] = updateForeignOwner(task)
        statement[Tasks.executorId] = updateForeignExecutor(task)
        statement[Tasks.reporterId] = updateForeignReporter(task)
        statement[Tasks.status] = task.status
        statement[Tasks.type] = task.type
        statement[Tasks.timings] = updateForeignTimings(task)
        statement[Tasks.lock] = task.lock.asString()
    }

    private fun save(task: TAppTask): DbTaskResponse {
        return safeTransaction({
            Tasks.insert {
                if (task.id != TAppTaskId.NONE) {
                    it[id] = task.id.asString()
                }
                fillTasksUpdateBuilder(it, task)
            }

            val result = (Tasks innerJoin Owners innerJoin Reporters innerJoin Executors innerJoin Timings)
                .select { Tasks.id.eq(task.id.asString()) }.single()

            DbTaskResponse(Tasks.from(result), true)
        }, {
            DbTaskResponse(
                result = null,
                isSuccess = false,
                errors = listOf(TAppError(message = message ?: localizedMessage))
            )
        })
    }

    private fun updateDb(task: TAppTask): DbTaskResponse {
        Tasks.update({ Tasks.id.eq(task.id.asString()) }) {
            fillTasksUpdateBuilder(it, task)
        }

        val result = (Tasks innerJoin Owners innerJoin Reporters innerJoin Executors innerJoin Timings)
            .select { Tasks.id.eq(task.id.asString()) }.single()

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
                    message = "Not found"
                )
            )
        )
    }
}
