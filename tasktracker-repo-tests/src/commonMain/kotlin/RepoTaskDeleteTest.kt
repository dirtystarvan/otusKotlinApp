package ru.ac1d.tasktracker.common.repo.test

import IInitObjects
import kotlinx.coroutines.runBlocking
import ru.ac1d.tasktracker.common.models.TAppError
import ru.ac1d.tasktracker.common.models.TAppTask
import ru.ac1d.tasktracker.common.repo.DbTaskIdRequest
import ru.ac1d.tasktracker.common.repo.ITaskRepo
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoTaskDeleteTest {
    abstract val repo: ITaskRepo

    companion object: BaseInitTaskObjs("delete"), IInitObjects<TAppTask> {
        private val expected = initTestModel()
        override val initObjects = listOf(expected)

    }

    @Test
    fun deleteSuccessTest() {
        //TODO lock
        val result = runBlocking { repo.deleteTask(DbTaskIdRequest(expected)) }

        assertEquals(true, result.isSuccess)
        assertEquals(expected, result.result)
        assertEquals(0, result.errors.size)
    }


    @Test
    fun deleteNotFoundTest() {
        val result = runBlocking { repo.deleteTask(DbTaskIdRequest(initTestModel(suffix = "notfound"))) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(TAppError(field = "id", message = "Not Found")),
            result.errors
        )
    }
}
