package ru.ac1d.tasktracker.biz.repo

import TaskRepoInMemory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.ac1d.tasktracker.biz.TAppTaskProcessor
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.*
import kotlin.test.assertEquals

open class BizRepoTest(private val initObjects: List<TAppTask> = emptyList()) {
    private val repoTest by lazy {
        TaskRepoInMemory(initObjects = initObjects, randomUUID = { testUUID })
    }

    protected val processor by lazy {
        TAppTaskProcessor(TAppSettings(repoTest = repoTest))
    }

    private val testUUID = "10000000-0000-0000-0000-000000000001"
    protected val testString = "test"
    protected val testType = TAppTaskType.TASK

    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun notFoundTest(command: TAppCommand) = runTest {
        val context = TrackerAppContext(
            command = command,
            workMode = TAppWorkMode.TEST,
            taskRequest = TAppTask(
                title = testString,
                description = testString,
                type = testType,
                lock = TAppTaskLock(testUUID)
            )
        )

        processor.exec(context)

        assertEquals(TAppStates.FAILING, context.status)
        assertEquals(TAppTask(), context.taskResponse)
        assertEquals(1, context.errors.size)
        assertEquals("id", context.errors.first().field)
    }
}