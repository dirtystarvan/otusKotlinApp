package ru.ac1d.tasktracker.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskReadBizRepoTest: BizRepoTest(initObjects = listOf(task)) {

    companion object {
        val task = TAppTask(
            id = TAppTaskId("123"),
            title = "test",
            description = "test",
            type = TAppTaskType.TASK
        )
    }

    private val command = TAppCommand.READ

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoReadSuccessTest() = runTest {
        val context = TrackerAppContext(
            command = command,
            workMode = TAppWorkMode.TEST,
            taskRequest = TAppTask(
                id = TAppTaskId("123"),
            ),
        )

        processor.exec(context)

        assertEquals(TAppStates.FINISHING, context.status)
        assertEquals(task.id, context.taskResponse.id)
        assertEquals(task.title, context.taskResponse.title)
        assertEquals(task.description, context.taskResponse.description)
        assertEquals(task.type, context.taskResponse.type)
    }

    @Test
    fun repoReadNotFoundTest() = notFoundTest(command =  command)
}