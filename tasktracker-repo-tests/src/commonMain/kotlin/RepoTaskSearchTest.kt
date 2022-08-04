import kotlinx.coroutines.runBlocking
import ru.ac1d.tasktracker.common.models.TAppTask
import ru.ac1d.tasktracker.common.models.TAppTaskType
import ru.ac1d.tasktracker.common.models.TAppUserId
import ru.ac1d.tasktracker.common.repo.DbTaskFilterRequest
import ru.ac1d.tasktracker.common.repo.ITaskRepo
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoTaskSearchTest {
    abstract val repo: ITaskRepo
    protected open val expectedTaskList: List<TAppTask> = initObjects

    companion object: BaseInitTaskObjs("search") {
        private val searchOwnerId = TAppUserId("searchUser-123")
        val initObjects: List<TAppTask> = listOf(
            initTestModel("task1"),
            initTestModel("task2", owner = searchOwnerId),
            initTestModel("task1", taskType = TAppTaskType.STORY),
        )
    }

    @Test
    fun searchByOwnerTest() {
        val result = runBlocking { repo.searchTask(DbTaskFilterRequest(ownerId = searchOwnerId)) }

        assertEquals(true, result.isSuccess)
        assertEquals(0, result.errors.size)

        val expected = expectedTaskList.filter { it.ownerId == searchOwnerId }.sortedBy { it.id.asString() }
        assertEquals(expected, result.result?.sortedBy { it.id.asString() })
    }

    @Test
    fun searchByTaskTypeTest() {
        val searchType = TAppTaskType.STORY

        val result = runBlocking { repo.searchTask(DbTaskFilterRequest(type = searchType)) }

        assertEquals(true, result.isSuccess)
        assertEquals(0, result.errors.size)

        val expected = expectedTaskList.filter { it.type == searchType }.sortedBy { it.id.asString() }
        assertEquals(expected, result.result?.sortedBy { it.id.asString() })
    }

    @Test
    fun notFoundTest() {
        val result = runBlocking { repo.searchTask(DbTaskFilterRequest(titleFilter = "")) }

        assertEquals(false, result.isSuccess)
        assertEquals(0, result.result?.size)
    }
}

