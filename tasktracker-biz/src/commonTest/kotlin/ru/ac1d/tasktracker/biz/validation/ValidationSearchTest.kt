package ru.ac1d.tasktracker.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.ac1d.tasktracker.biz.TAppTaskProcessor
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.TAppCommand
import ru.ac1d.tasktracker.common.models.TAppStates
import ru.ac1d.tasktracker.common.models.TAppTaskFilter
import ru.ac1d.tasktracker.common.models.TAppWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ValidationSearchTest {
    private val processor = TAppTaskProcessor()
    private val command = TAppCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val context = TrackerAppContext(
            command = command,
            status = TAppStates.NONE,
            workMode = TAppWorkMode.TEST,
            taskFilterRequest = TAppTaskFilter()
        )
        processor.exec(context)
        assertEquals(0, context.errors.size)
        assertNotEquals(TAppStates.FAILING, context.status)
    }
}
