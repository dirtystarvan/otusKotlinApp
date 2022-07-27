import ru.ac1d.tasktracker.biz.TAppTaskProcessor
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppError
import ru.ac1d.tasktracker.common.models.TAppTaskId
import ru.ac1d.tasktracker.common.models.TAppWorkMode
import ru.ac1d.tasktracker.common.stubs.TAppStubs

class TaskService {
    private val processor = TAppTaskProcessor()

    suspend fun exec(context: TrackerAppContext) = processor.exec(context)

    fun createTask(appContext: TrackerAppContext) : TrackerAppContext {
        val response = when(appContext.workMode) {
            TAppWorkMode.PROD -> TODO()
            TAppWorkMode.TEST -> appContext.taskRequest
            TAppWorkMode.STUB -> TaskStub.getModel()
        }

        return appContext.successResponse {
            taskResponse = response
        }
    }

    fun readTask(appContext: TrackerAppContext, buildError: () -> TAppError): TrackerAppContext {
        val requestedId = appContext.taskRequest.id

        return when (appContext.stubCase) {
            TAppStubs.SUCCESS -> appContext.successResponse {
                taskResponse = TaskStub.getModel().apply { id = requestedId }
            }
            else -> appContext.errorResponse(buildError) {
                it.copy(field = "task.id", message = notFoundError(requestedId.asString()))
            }
        }
    }

    fun updateTask(appContext: TrackerAppContext, buildError: () -> TAppError) = when (appContext.stubCase) {
        TAppStubs.SUCCESS -> appContext.successResponse {
            taskResponse = TaskStub.getModel {
                if (taskRequest.id != TAppTaskId.NONE) id = taskRequest.id
                if (taskRequest.title.isNotEmpty()) title = taskRequest.title
            }
        }
        else -> appContext.errorResponse(buildError) {
            it.copy(field = "task.id", message = notFoundError(appContext.taskRequest.id.asString()))
        }
    }

    fun deleteTask(appContext: TrackerAppContext, buildError: () -> TAppError): TrackerAppContext = when (appContext.stubCase) {
        TAppStubs.SUCCESS -> appContext.successResponse {
            taskResponse = TaskStub.getModel { id = appContext.taskRequest.id }
        }
        else -> appContext.errorResponse(buildError) {
            it.copy(field = "task.id", message = notFoundError(appContext.taskRequest.id.asString()))
        }
    }

    fun searchTask(appContext: TrackerAppContext, buildError: () -> TAppError): TrackerAppContext {
        val filter = appContext.taskFilterRequest
        val searchableString = filter.searchString

        return when (appContext.stubCase) {
            TAppStubs.SUCCESS -> appContext.successResponse {
                taskListResponse.addAll(TaskStub.getModels())
            } else -> appContext.errorResponse(buildError) {
                it.copy(message = "Nothing found by $searchableString")
            }
        }
    }
}