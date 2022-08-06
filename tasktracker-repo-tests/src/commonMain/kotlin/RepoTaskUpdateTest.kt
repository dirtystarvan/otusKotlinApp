package ru.ac1d.tasktracker.common.repo.test

import IInitObjects
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import ru.ac1d.tasktracker.common.models.TAppError
import ru.ac1d.tasktracker.common.models.TAppTask
import ru.ac1d.tasktracker.common.repo.DbTaskRequest
import ru.ac1d.tasktracker.common.repo.ITaskRepo
import kotlin.test.assertEquals

abstract class RepoTaskUpdateTest {
    abstract val repo: ITaskRepo

    companion object: BaseInitTaskObjs("update"), IInitObjects<TAppTask> {
        private val updateObj = initTestModel()

        override val initObjects = listOf(updateObj)
    }

    @Test
    fun updateSuccessTest() {
        val result = runBlocking { repo.updateTask(DbTaskRequest(updateObj)) }

        assertEquals(true, result.isSuccess)
        assertEquals(updateObj.id, result.result?.id)
        assertEquals(updateObj.title, result.result?.title)
        assertEquals(updateObj.description, result.result?.description)
        assertEquals(updateObj.ownerId, result.result?.ownerId)
        assertEquals(updateObj.reporterId, result.result?.reporterId)
        assertEquals(updateObj.executorId, result.result?.executorId)
        assertEquals(updateObj.status, result.result?.status)
        assertEquals(updateObj.type, result.result?.type)
        //TODO lock
        assertEquals(0, result.errors.size)
    }

    @Test
    fun updateNotFoundTest() {
        val result = runBlocking { repo.updateTask(DbTaskRequest(initTestModel(suffix = "notfound"))) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(TAppError(field = "id", message = "Not found")),
            result.errors
        )
    }
}
