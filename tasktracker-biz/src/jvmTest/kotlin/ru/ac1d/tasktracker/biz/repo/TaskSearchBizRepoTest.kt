package ru.ac1d.tasktracker.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.ac1d.tasktracker.common.TrackerAppContext
import kotlin.test.Test
import ru.ac1d.tasktracker.common.models.*
import kotlin.test.assertEquals

class TaskSearchBizRepoTest: BizRepoTest(initObjects = listOf(initTask)) {
    companion object {
        private val command = TAppCommand.SEARCH
        private val testId = TAppTaskId("123")
        private val testType = TAppTaskType.TASK

        val initTask = TAppTask(
            id = testId,
            title = "test",
            description = "test",
            type = testType,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoSearchSuccessTest() = runTest {
        val context = TrackerAppContext(
            command = command,
            workMode = TAppWorkMode.TEST,
            taskFilterRequest = TAppTaskFilter(
                searchString = "test",
                taskType = testType
            ),
        )

        processor.exec(context)

        assertEquals(TAppStates.FINISHING, context.status)
        assertEquals(1, context.taskListResponse.size)

        val taskFound = context.taskListResponse.first()
        assertEquals(initTask.id, taskFound.id)
        assertEquals(initTask.title, taskFound.title)
        assertEquals(initTask.description, taskFound.description)
        assertEquals(initTask.type, taskFound.type)
    }
}