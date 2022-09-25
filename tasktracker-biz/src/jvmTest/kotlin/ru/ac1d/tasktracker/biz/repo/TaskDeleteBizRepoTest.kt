package ru.ac1d.tasktracker.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TaskDeleteBizRepoTest: BizRepoTest(initObjects = listOf(initTask)) {

    companion object {
        private const val uuidBefore = "10000000-0000-0000-0000-000000000002"
        private const val uuidBad = "10000000-0000-0000-0000-000000000003"

        private val command = TAppCommand.DELETE
        private val testId = TAppTaskId("123")

        val initTask = TAppTask(
            id = testId,
            title = "test",
            description = "test",
            type = TAppTaskType.TASK,
            lock = TAppTaskLock(uuidBefore),
        )
    }

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val forDelete = TAppTask(
            id = testId,
            lock = TAppTaskLock(uuidBefore)
        )

        val context = TrackerAppContext(
            command = command,
            workMode = TAppWorkMode.TEST,
            taskRequest = forDelete
        )

        processor.exec(context)

        assertEquals(TAppStates.FINISHING, context.status)
        assertTrue { context.errors.isEmpty() }
        assertEquals(initTask.id, context.taskResponse.id)
        assertEquals(initTask.title, context.taskResponse.title)
        assertEquals(initTask.description, context.taskResponse.description)
        assertEquals(initTask.type, context.taskResponse.type)
        assertEquals(uuidBefore, context.taskResponse.lock.asString())
    }

    @Test
    fun repoDeleteConcurrentTest() = runTest {
        val forDelete = TAppTask(
            id = testId,
            lock = TAppTaskLock(uuidBad)
        )

        val context = TrackerAppContext(
            command = command,
            workMode = TAppWorkMode.TEST,
            taskRequest = forDelete
        )

        processor.exec(context)

        assertEquals(TAppStates.FAILING, context.status)
        assertEquals(1, context.errors.size)
        assertEquals("concurrency", context.errors.first().group)
        assertEquals(initTask.id, context.taskResponse.id)
        assertEquals(initTask.title, context.taskResponse.title)
        assertEquals(initTask.description, context.taskResponse.description)
        assertEquals(initTask.type, context.taskResponse.type)
        assertEquals(uuidBefore, context.taskResponse.lock.asString())
    }

    @Test
    fun repoDeleteNotFoundTest() = notFoundTest(command)
}