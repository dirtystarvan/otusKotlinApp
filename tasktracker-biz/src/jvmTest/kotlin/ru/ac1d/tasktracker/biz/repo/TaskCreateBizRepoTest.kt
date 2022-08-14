package ru.ac1d.tasktracker.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class TaskCreateBizRepoTest: BizRepoTest() {

    @Test
    fun repoCreateSuccessTest() = runTest {
        val context = TrackerAppContext(
            command = TAppCommand.CREATE,
            workMode = TAppWorkMode.TEST,
            taskRequest = TAppTask(
                title = testString,
                description = testString,
                type = testType,
            )
        )

        processor.exec(context)

        println(context.errors)

        assertEquals(TAppStates.FINISHING, context.status)
        assertNotEquals(TAppTaskId.NONE, context.taskResponse.id)
        assertEquals(testString, context.taskResponse.title)
        assertEquals(testString, context.taskResponse.description)
        assertEquals(testType, context.taskResponse.type)
        assertNotNull(context.taskResponse.lock.asString())
    }
}