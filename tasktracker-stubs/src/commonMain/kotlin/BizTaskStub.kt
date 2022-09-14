import TaskCreateExample.TASK_CREATE_EXAMPLE
import ru.ac1d.tasktracker.common.models.TAppTask
import ru.ac1d.tasktracker.common.models.TAppTaskId
import ru.ac1d.tasktracker.common.models.TAppTaskType

object BizTaskStub {
    fun get(): TAppTask = TASK_CREATE_EXAMPLE.copy()

    fun prepareResult(block: TAppTask.() -> Unit): TAppTask = get().apply(block)

    fun prepareSearchList(filter: String, type: TAppTaskType) = listOf(
        tAppTaskExample("123", filter, type),
        tAppTaskExample("456", filter, type),
        tAppTaskExample("789", filter, type),
    )

    private fun tAppTaskExample(id: String, filter: String, type: TAppTaskType) =
        tAppTaskGen(TASK_CREATE_EXAMPLE, id = id, filter = filter, type = type)

    private fun tAppTaskGen(base: TAppTask, id: String, filter: String, type: TAppTaskType) = base.copy(
        id = TAppTaskId(id),
        title = "$filter $id",
        description = "desc $filter $id",
        type = type
    )
}
