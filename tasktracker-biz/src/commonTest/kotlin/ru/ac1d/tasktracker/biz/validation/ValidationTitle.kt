package ru.ac1d.tasktracker.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.ac1d.tasktracker.biz.TAppTaskProcessor
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val testId = TAppTaskId("123")
private val testTaskType = TAppTaskType.TASK

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleCorrect(command: TAppCommand, processor: TAppTaskProcessor) = runTest {
    val context = TrackerAppContext(
        command = command,
        status = TAppStates.NONE,
        workMode = TAppWorkMode.TEST,
        taskRequest = TAppTask(
            id = testId,
            title = "abc",
            description = "abc",
            type = testTaskType,
        ),
    )
    processor.exec(context)
    assertEquals(0, context.errors.size)
    assertNotEquals(TAppStates.FAILING, context.status)
    assertEquals("abc", context.taskValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleTrim(command: TAppCommand, processor: TAppTaskProcessor) = runTest {
    val context = TrackerAppContext(
        command = command,
        status = TAppStates.NONE,
        workMode = TAppWorkMode.TEST,
        taskRequest = TAppTask(
            id = testId,
            title = " \n\t abc \t\n ",
            description = "abc",
            type = testTaskType,
        ),
    )
    processor.exec(context)
    assertEquals(0, context.errors.size)
    assertNotEquals(TAppStates.FAILING, context.status)
    assertEquals("abc", context.taskValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleEmpty(command: TAppCommand, processor: TAppTaskProcessor) = runTest {
    val context = TrackerAppContext(
        command = command,
        status = TAppStates.NONE,
        workMode = TAppWorkMode.TEST,
        taskRequest = TAppTask(
            id = testId,
            title = "",
            description = "abc",
            type = testTaskType,
        ),
    )
    processor.exec(context)
    assertEquals(1, context.errors.size)
    assertEquals(TAppStates.FAILING, context.status)
    val error = context.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleSymbols(command: TAppCommand, processor: TAppTaskProcessor) = runTest {
    val context = TrackerAppContext(
        command = command,
        status = TAppStates.NONE,
        workMode = TAppWorkMode.TEST,
        taskRequest = TAppTask(
            id = testId,
            title = "!@#$%^&*(),.{}",
            description = "abc",
            type = testTaskType,
        ),
    )
    processor.exec(context)
    assertEquals(1, context.errors.size)
    assertEquals(TAppStates.FAILING, context.status)
    val error = context.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}