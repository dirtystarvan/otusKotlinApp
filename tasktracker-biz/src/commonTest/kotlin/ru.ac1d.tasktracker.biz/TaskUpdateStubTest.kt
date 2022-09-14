package ru.ac1d.tasktracker.biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.*
import ru.ac1d.tasktracker.common.stubs.TAppStubs
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TaskUpdateStubTest {
    private val processor = TAppTaskProcessor()

    private val testId = TAppTaskId("111")
    private val testTitle = "Test title"
    private val testDescription = "Test description"
    private val testTaskType = TAppTaskType.TASK
    private val testReporter = TAppUserId("666")
    private val testTaskExecutor = TAppUserId("111")
    private val testTaskStatus = TAppTaskStatus.OPEN

    @Test
    fun update() = runTest {
        val context = TrackerAppContext(
            command = TAppCommand.CREATE,
            status = TAppStates.NONE,
            workMode = TAppWorkMode.STUB,
            stubCase = TAppStubs.SUCCESS,
            taskRequest = TAppTask(
                id = testId,
                title = testTitle,
                description = testDescription,
                type = testTaskType,
                reporterId = testReporter,
                executorId = testTaskExecutor,
                status = testTaskStatus,
            ),
        )

        processor.exec(context)
        assertEquals(testId, context.taskResponse.id)
        assertEquals(testTitle, context.taskResponse.title)
        assertEquals(testDescription, context.taskResponse.description)
        assertEquals(testTaskType, context.taskResponse.type)
        assertEquals(testReporter, context.taskResponse.reporterId)
        assertEquals(testTaskExecutor, context.taskResponse.executorId)
    }

    @Test
    fun badId() = runTest {
        val context = TrackerAppContext(
            command = TAppCommand.UPDATE,
            status = TAppStates.NONE,
            workMode = TAppWorkMode.STUB,
            stubCase = TAppStubs.BAD_ID,
            taskRequest = TAppTask(),
        )
        processor.exec(context)
        assertEquals(TAppTask(), context.taskResponse)
        assertEquals("id", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun badTitle() = runTest {
        val context = TrackerAppContext(
            command = TAppCommand.UPDATE,
            status = TAppStates.NONE,
            workMode = TAppWorkMode.STUB,
            stubCase = TAppStubs.BAD_TITLE,
            taskRequest = TAppTask(
                id = testId,
                title = "",
                description = testDescription,
                type = testTaskType,
            ),
        )
        processor.exec(context)
        assertEquals(TAppTask(), context.taskResponse)
        assertEquals("title", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }
    @Test
    fun badDescription() = runTest {
        val context = TrackerAppContext(
            command = TAppCommand.UPDATE,
            status = TAppStates.NONE,
            workMode = TAppWorkMode.STUB,
            stubCase = TAppStubs.BAD_DESCRIPTION,
            taskRequest = TAppTask(
                id = testId,
                title = testTitle,
                description = "",
                type = testTaskType,
            ),
        )
        processor.exec(context)
        assertEquals(TAppTask(), context.taskResponse)
        assertEquals("description", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val context = TrackerAppContext(
            command = TAppCommand.UPDATE,
            status = TAppStates.NONE,
            workMode = TAppWorkMode.STUB,
            stubCase = TAppStubs.DB_ERROR,
            taskRequest = TAppTask(
                id = testId,
            ),
        )
        processor.exec(context)
        assertEquals(TAppTask(), context.taskResponse)
        assertEquals("internal", context.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val context = TrackerAppContext(
            command = TAppCommand.UPDATE,
            status = TAppStates.NONE,
            workMode = TAppWorkMode.STUB,
            stubCase = TAppStubs.BAD_SEARCH_STRING,
            taskRequest = TAppTask(
                id = testId,
                title = testTitle,
                description = testDescription,
                type = testTaskType,
            ),
        )
        processor.exec(context)
        assertEquals(TAppTask(), context.taskResponse)
        assertEquals("stub", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }
}
