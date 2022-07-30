import kotlinx.datetime.LocalDateTime
import org.junit.Test
import ru.ac1d.api.v1.models.*
import ru.ac1d.tasktracker.common.NONE
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.*
import ru.ac1d.tasktracker.common.stubs.TAppStubs
import kotlin.test.assertEquals

class MappersTest {
    @Test
    fun fromTransportTest() {
        val request = TaskCreateRequest(
            requestId = "1369",
            debug = TaskDebug(
                mode = TaskRequestDebugMode.STUB,
                stub = TaskRequestDebugStubs.SUCCESS,
            ),
            task = TaskCreateObject(
                baseTask = TaskUpdatable(
                    title = "title",
                    description = "test",
                    type = TaskType.TASK,
                    reporterId = "1",
                    executorId = "2",
                    status = TaskStatus.OPEN,
                )
            )
        )

        val context = TrackerAppContext()

        context.fromTransport(request)

        assertEquals(TAppStubs.SUCCESS, context.stubCase)
        assertEquals(TAppWorkMode.STUB, context.workMode)
        assertEquals("test", context.taskRequest.description)
        assertEquals(LocalDateTime.NONE, context.taskRequest.timings.start.asLocalDate())
        assertEquals(TAppTaskStatus.OPEN, context.taskRequest.status)
    }

    @Test
    fun toTransportTest() {
        val context = TrackerAppContext(
            requestId = TAppRequestId("6913"),
            command = TAppCommand.CREATE,
            taskResponse = TAppTask(
                id = TAppTaskId("1"),
                title = "title",
                description = "test",
                type = TAppTaskType.TASK,
            ),
            errors = mutableListOf(
                TAppError(
                    code = "error",
                    group = "test",
                    field = "type",
                    message = "testerr",
                )
            ),
            status = TAppStates.FAILING
        )

        val response = context.toTransport() as TaskCreateResponse

        assertEquals("6913", response.requestId)
        assertEquals("title", response.task?.baseTask?.title)
        assertEquals("test", response.task?.baseTask?.description)
        assertEquals(TaskType.TASK, response.task?.baseTask?.type)
        assertEquals(1, response.errors?.size)
        assertEquals("error", response.errors?.firstOrNull()?.code)
        assertEquals("testerr", response.errors?.firstOrNull()?.message)
    }
}
