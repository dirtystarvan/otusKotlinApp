import kotlinx.coroutines.runBlocking
import ru.ac1d.tasktracker.common.models.TAppError
import ru.ac1d.tasktracker.common.repo.DbTaskIdRequest
import ru.ac1d.tasktracker.common.repo.ITaskRepo
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoTaskDeleteTest {
    abstract val repo: ITaskRepo

    companion object: BaseInitTaskObjs("delete")

    @Test
    fun deleteSuccessTest() {
        //TODO lock
        val expected = initTestModel()
        val result = runBlocking { repo.deleteTask(DbTaskIdRequest(expected)) }


        assertEquals(true, result.isSuccess)
        assertEquals(expected, result.result)
        assertEquals(0, result.errors.size)
    }


    @Test
    fun deleteNotFoundTest() {
        val result = runBlocking { repo.readTask(DbTaskIdRequest(initTestModel(suffix = "notfound"))) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(TAppError(field = "id", message = "Not Found")),
            result.errors
        )
    }
}
