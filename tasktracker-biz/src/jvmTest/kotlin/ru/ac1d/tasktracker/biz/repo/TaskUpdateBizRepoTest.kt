package ru.ac1d.tasktracker.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TaskUpdateBizRepoTest: BizRepoTest(initObjects = listOf(initTask)) {

    companion object {
        private const val uuidBefore = "10000000-0000-0000-0000-000000000002"
        private const val uuidAfter = "10000000-0000-0000-0000-000000000001"
        private const val uuidBad = "10000000-0000-0000-0000-000000000003"

        private val testId = TAppTaskId("123")
        private val command = TAppCommand.UPDATE

        val initTask = TAppTask(
            id = testId,
            title = "test",
            description = "test",
            type = TAppTaskType.TASK,
            lock = TAppTaskLock(uuidBefore),
        )
    }


    @Test
    fun repoUpdateSuccessTest() = runTest {
        val forUpdate = TAppTask(
            id = testId,
            title = "updated",
            description = "updated",
            type = TAppTaskType.TASK,
            lock = TAppTaskLock(uuidBefore),
        )

        val context = TrackerAppContext(
            command = command,
            workMode = TAppWorkMode.TEST,
            taskRequest = forUpdate,
        )

        processor.exec(context)

        assertEquals(TAppStates.FINISHING, context.status)
        assertEquals(forUpdate.id, context.taskResponse.id)
        assertEquals(forUpdate.title, context.taskResponse.title)
        assertEquals(forUpdate.description, context.taskResponse.description)
        assertEquals(forUpdate.type, context.taskResponse.type)
        assertEquals(uuidAfter, context.taskResponse.lock.asString())
    }

    @Test
    fun repoUpdateConcurrentTest() = runTest {
        val forUpdate = TAppTask(
            id = testId,
            title = "updated",
            description = "updated",
            type = TAppTaskType.TASK,
            lock = TAppTaskLock(uuidBad),
        )

        val context = TrackerAppContext(
            command = command,
            workMode = TAppWorkMode.TEST,
            taskRequest = forUpdate,
        )

        processor.exec(context)
        assertEquals(TAppStates.FAILING, context.status)
        assertEquals(initTask.id, context.taskResponse.id)
        assertEquals(initTask.title, context.taskResponse.title)
        assertEquals(initTask.description, context.taskResponse.description)
        assertEquals(initTask.type, context.taskResponse.type)
        assertEquals(uuidBefore, context.taskResponse.lock.asString())
        assertEquals("concurrency", context.errors.first().group)
    }

    @Test
    fun repoUpdateNotFoundTest() = notFoundTest(command = command)
}