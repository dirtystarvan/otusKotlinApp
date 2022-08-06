package ru.ac1d.tasktracker.common.repo.test

import IInitObjects
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import ru.ac1d.tasktracker.common.models.TAppError
import ru.ac1d.tasktracker.common.models.TAppTask
import ru.ac1d.tasktracker.common.models.TAppTaskId
import ru.ac1d.tasktracker.common.repo.DbTaskIdRequest
import ru.ac1d.tasktracker.common.repo.ITaskRepo
import kotlin.test.assertEquals

abstract class RepoTaskReadTest {
    abstract val repo: ITaskRepo

    companion object: BaseInitTaskObjs("read"), IInitObjects<TAppTask> {
        private val expected = initTestModel()
        private val badId = TAppTaskId("bad")

        override val initObjects: List<TAppTask> = listOf(expected)
    }

    @Test
    fun readSuccessTest() {
        val result = runBlocking { repo.readTask(DbTaskIdRequest(expected.id)) }

        assertEquals(true, result.isSuccess)
        assertEquals(expected, result.result)
        assertEquals(0, result.errors.size)
    }

    @Test
    fun notFoundTest() {
        val result = runBlocking { repo.readTask(DbTaskIdRequest(badId)) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(TAppError(field = "id", message = "Not found")),
            result.errors
        )
    }
}
