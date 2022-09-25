package ru.ac1d.tasktracker.biz

import TaskCreateExample.TASK_CREATE_EXAMPLE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.*
import ru.ac1d.tasktracker.common.stubs.TAppStubs
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TaskReadStubTest {

    private val processor = TAppTaskProcessor()
    private val testId = TAppTaskId("111")

    @Test
    fun read() = runTest {

        val context = TrackerAppContext(
            command = TAppCommand.READ,
            status = TAppStates.NONE,
            workMode = TAppWorkMode.STUB,
            stubCase = TAppStubs.SUCCESS,
            taskRequest = TAppTask(
                id = testId,
            ),
        )
        processor.exec(context)
        assertEquals(TASK_CREATE_EXAMPLE.id, context.taskResponse.id)
        assertEquals(TASK_CREATE_EXAMPLE.title, context.taskResponse.title)
        assertEquals(TASK_CREATE_EXAMPLE.description, context.taskResponse.description)
        assertEquals(TASK_CREATE_EXAMPLE.type, context.taskResponse.type)
    }

    @Test
    fun badId() = runTest {
        val context = TrackerAppContext(
            command = TAppCommand.READ,
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
    fun databaseError() = runTest {
        val context = TrackerAppContext(
            command = TAppCommand.READ,
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
            command = TAppCommand.READ,
            status = TAppStates.NONE,
            workMode = TAppWorkMode.STUB,
            stubCase = TAppStubs.BAD_TITLE,
            taskRequest = TAppTask(
                id = testId,
            ),
        )
        processor.exec(context)
        assertEquals(TAppTask(), context.taskResponse)
        assertEquals("stub", context.errors.firstOrNull()?.field)
    }
}
