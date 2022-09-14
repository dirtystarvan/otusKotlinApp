package ru.ac1d.tasktracker.biz

import TaskCreateExample.TASK_CREATE_EXAMPLE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.*
import ru.ac1d.tasktracker.common.stubs.TAppStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
class TaskSearchStubTest {
    private val processor = TAppTaskProcessor()
    private val filter = TAppTaskFilter(searchString = "task")

    @Test
    fun search() = runTest {
        val context = TrackerAppContext(
            command = TAppCommand.SEARCH,
            status = TAppStates.NONE,
            workMode = TAppWorkMode.STUB,
            stubCase = TAppStubs.SUCCESS,
            taskFilterRequest = filter,
        )
        processor.exec(context)
        assertTrue(context.taskListResponse.size > 1)
        val first = context.taskListResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains(filter.searchString))
        assertTrue(first.description.contains(filter.searchString))
        assertEquals(TASK_CREATE_EXAMPLE.type, first.type)
    }

    @Test
    fun badId() = runTest {
        val context = TrackerAppContext(
            command = TAppCommand.SEARCH,
            status = TAppStates.NONE,
            workMode = TAppWorkMode.STUB,
            stubCase = TAppStubs.BAD_ID,
            taskFilterRequest = filter,
        )
        processor.exec(context)
        assertEquals(TAppTask(), context.taskResponse)
        assertEquals("id", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val context = TrackerAppContext(
            command = TAppCommand.SEARCH,
            status = TAppStates.NONE,
            workMode = TAppWorkMode.STUB,
            stubCase = TAppStubs.DB_ERROR,
            taskFilterRequest = filter,
        )
        processor.exec(context)
        assertEquals(TAppTask(), context.taskResponse)
        assertEquals("internal", context.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val context = TrackerAppContext(
            command = TAppCommand.SEARCH,
            status = TAppStates.NONE,
            workMode = TAppWorkMode.STUB,
            stubCase = TAppStubs.BAD_TITLE,
            taskFilterRequest = filter,
        )
        processor.exec(context)
        assertEquals(TAppTask(), context.taskResponse)
        assertEquals("stub", context.errors.firstOrNull()?.field)
    }
}
