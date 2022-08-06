package ru.ac1d.tasktracker.common.repo.test

import kotlinx.coroutines.runBlocking
import ru.ac1d.tasktracker.common.models.TAppTaskId
import ru.ac1d.tasktracker.common.repo.DbTaskRequest
import ru.ac1d.tasktracker.common.repo.ITaskRepo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


abstract class RepoTaskCreateTest {
    abstract val repo: ITaskRepo

    @Test
    fun createSuccessTest() {
        val expected = initTestModel()
        val result = runBlocking { repo.createTask(DbTaskRequest(expected)) }

        assertEquals(true, result.isSuccess)
        assertEquals(expected.title, result.result?.title)
        assertEquals(expected.description, result.result?.description)
        assertEquals(expected.ownerId, result.result?.ownerId)
        assertEquals(expected.reporterId, result.result?.reporterId)
        assertEquals(expected.executorId, result.result?.executorId)
        assertEquals(expected.status, result.result?.status)
        assertEquals(expected.type, result.result?.type)
        assertNotEquals(TAppTaskId.NONE, result.result?.id)
        assertEquals(0, result.errors.size)
    }

    companion object: BaseInitTaskObjs("create")
}
