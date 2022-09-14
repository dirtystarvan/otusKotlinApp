package ru.ac1d.tasktracker.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.ac1d.tasktracker.biz.TAppTaskProcessor
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val testTaskType = TAppTaskType.TASK

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdCorrect(command: TAppCommand, processor: TAppTaskProcessor) = runTest {
    val context = TrackerAppContext(
        command = command,
        status = TAppStates.NONE,
        workMode = TAppWorkMode.TEST,
        taskRequest = TAppTask(
            id = TAppTaskId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            type = testTaskType,
        ),
    )
    processor.exec(context)
    assertEquals(0, context.errors.size)
    assertNotEquals(TAppStates.FAILING, context.status)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdTrim(command: TAppCommand, processor: TAppTaskProcessor) = runTest {
    val context = TrackerAppContext(
        command = command,
        status = TAppStates.NONE,
        workMode = TAppWorkMode.TEST,
        taskRequest = TAppTask(
            id = TAppTaskId(" \n\t 123-234-abc-ABC \n\t "),
            title = "abc",
            description = "abc",
            type = testTaskType,
        ),
    )
    processor.exec(context)
    assertEquals(0, context.errors.size)
    assertNotEquals(TAppStates.FAILING, context.status)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdEmpty(command: TAppCommand, processor: TAppTaskProcessor) = runTest {
    val context = TrackerAppContext(
        command = command,
        status = TAppStates.NONE,
        workMode = TAppWorkMode.TEST,
        taskRequest = TAppTask(
            id = TAppTaskId(""),
            title = "abc",
            description = "abc",
            type = testTaskType,
        ),
    )
    processor.exec(context)
    assertEquals(1, context.errors.size)
    assertEquals(TAppStates.FAILING, context.status)
    val error = context.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdFormat(command: TAppCommand, processor: TAppTaskProcessor) = runTest {
    val context = TrackerAppContext(
        command = command,
        status = TAppStates.NONE,
        workMode = TAppWorkMode.TEST,
        taskRequest = TAppTask(
            id = TAppTaskId("!@#\$%^&*(),.{}"),
            title = "abc",
            description = "abc",
            type = testTaskType,
        ),
    )
    processor.exec(context)
    assertEquals(1, context.errors.size)
    assertEquals(TAppStates.FAILING, context.status)
    val error = context.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
