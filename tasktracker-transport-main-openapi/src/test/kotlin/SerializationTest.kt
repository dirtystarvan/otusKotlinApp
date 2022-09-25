import org.junit.jupiter.api.Test
import ru.ac1d.api.v1.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals


class SerializationTest {

    private val createRequest = TaskCreateRequest(
        task = TaskCreateObject(
            baseTask = TaskUpdatable(
                title = "Title",
                description = "Some description",
                type = TaskType.TASK,
                reporterId = "123",
                executorId = "234",
                status = TaskStatus.OPEN,
                timings = null,
                subtasks = null
            )
        )
    )

    @Test
    fun serializeTest() {
        val jsonString = jacksonSerialize(createRequest)
        println(jsonString)
        assertContains(jsonString, """"title":"Title"""")
    }

    @Test
    fun deserializeTest() {
        val jsonString = jacksonSerialize(createRequest)
        val decoded = jacksonDeserialize<TaskCreateRequest>(jsonString)
        println(decoded)
        assertEquals("Title", decoded.task?.baseTask?.title)
        assertEquals("Some description", decoded.task?.baseTask?.description)
        assertEquals(TaskType.TASK, decoded.task?.baseTask?.type)
    }
}
