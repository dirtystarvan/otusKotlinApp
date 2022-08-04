import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import ru.ac1d.tasktracker.common.repo.*
import kotlin.time.Duration
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import model.TaskEntity
import ru.ac1d.tasktracker.common.models.*
import kotlin.time.Duration.Companion.minutes

class TaskRepoInMemory(
    private val initObjects: List<TAppTask> = emptyList(),
    ttl: Duration = 2.minutes,
) : ITaskRepo {
    private val randomUUID: String
        get() = uuid4().toString()

    private val mutex = Mutex()

    private val cache = Cache.Builder()
        .expireAfterWrite(ttl)
        .build<String, TaskEntity>()

    init {
        initRepo()
    }

    fun initRepo() {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(task: TAppTask) {
        if (task.id.asString().isNotBlank()) {
            val entity = TaskEntity(task)
            cache.put(entity.id!!, entity)
        }
    }

    override suspend fun createTask(request: DbTaskRequest): DbTaskResponse {
        val key = randomUUID
        val taskCopy = request.task.copy(id = TAppTaskId(key)) //TODO: lock
        val entity = TaskEntity(taskCopy)

        mutex.withLock {
            cache.put(key, entity)
        }

        return DbTaskResponse(result = taskCopy, isSuccess = true)
    }

    override suspend fun readTask(request: DbTaskIdRequest): DbTaskResponse {
        val key = request.id.takeIf { it != TAppTaskId.NONE }?.asString() ?: return resultErrorEmptyId

        return cache.get(key)?.let {
            DbTaskResponse(
                result = it.toInternal(),
                isSuccess = true,
            )
        } ?: resultErrorNotFound
    }

    override suspend fun updateTask(request: DbTaskRequest): DbTaskResponse {
        val key = request.task.id.takeIf { it != TAppTaskId.NONE }?.asString() ?: return resultErrorEmptyId
        //TODO lock
        val newTask = request.task.copy()
        val entity = TaskEntity(newTask)

        mutex.withLock {
            val local = cache.get(key)
            when (local) {
                null -> return resultErrorNotFound
                else -> cache.put(key, entity)
            }
        }

        return DbTaskResponse(
            result = newTask,
            isSuccess = true
        )
    }

    override suspend fun deleteTask(request: DbTaskIdRequest): DbTaskResponse {
        val key = request.id.takeIf { it != TAppTaskId.NONE }?.asString() ?: return resultErrorEmptyId

        mutex.withLock {
            val local = cache.get(key) ?: return resultErrorNotFound
            cache.invalidate(key)

            //TODO: lock

            return DbTaskResponse(
                result = local.toInternal(),
                isSuccess = true,
                errors = emptyList()
            )
        }
    }

    override suspend fun searchTask(request: DbTaskFilterRequest): DbTaskListResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                request.ownerId.takeIf { it != TAppUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                request.type.takeIf { it != TAppTaskType.NONE }?.let {
                    it.name == entry.value.type
                } ?: true
            }
            .filter { entry ->
                request.titleFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()

        return DbTaskListResponse(
            result = result,
            isSuccess = true
        )
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
        //TODO lock
//        val resultErrorConcurrent = DbTaskResponse(
//            result = null,
//            isSuccess = false,
//            errors = listOf(
//                errorConcurrency(
//                    violationCode = "changed",
//                    description = "Object has changed during request handling"
//                ),
//            )
//        )



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