package ru.ac1d.tasktracker.app

import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import org.junit.Test
import ru.ac1d.api.v1.models.*


class ApplicationTest {
    @Test
    fun `help endpoint`() = testApplication {
        val response = client.get("/help")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Help is here!", response.bodyAsText())
    }

    @Test
    fun `task create endpoint`() = testApplication {
        val client = createClient() {
            install(ContentNegotiation) {
                jackson {
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                }
            }
        }

        val response : TaskCreateResponse = client.post("/v1/task/create") {
            contentType(ContentType.Application.Json)
            setBody(TaskCreateRequest(
                requestId = "123",
                requestType = "create",
                task = TaskCreateObject(
                    baseTask = TaskUpdatable(
                        title = "Test title",
                        description = "Test test test",
                        type = TaskType.TASK,
                        reporterId = "123",
                        executorId = "456",
                        status = TaskStatus.OPEN
                    )
                ),
                debug = TaskDebug(
                    mode = TaskRequestDebugMode.TEST
                )
            ))
        }.body()

        assertEquals(response.result, ResponseResult.SUCCESS)
        assertEquals(response.task?.baseTask?.reporterId, "123")
        assertEquals(response.task?.baseTask?.executorId, "456")
    }
}
