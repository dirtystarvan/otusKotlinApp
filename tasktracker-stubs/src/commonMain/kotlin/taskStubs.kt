import ru.ac1d.tasktracker.common.models.*

object TaskStub {
    private fun stubReady() = TAppTask(
        id = TAppTaskId("123"),
        title = "Task title",
        description = "Title description",
        type = TAppTaskType.TASK,
        reporterId = TAppUserId("6969"),
        executorId = TAppUserId("9696"),
        status = TAppTaskStatus.OPEN,
        timings = TAppTaskTimings(TAppTaskDate("2022-06-13"), TAppTaskDate("2022-06-27")),
        ownerId = TAppUserId("1"),
        permissionsClient = mutableSetOf(TAppTaskPermissionsClient.READ),
    )

    private fun someAnotherStub() = TAppTask(
        id = TAppTaskId("456"),
        title = "Another task title",
        description = "Another title description",
        type = TAppTaskType.TASK,
        reporterId = TAppUserId("6969"),
        executorId = TAppUserId("9696"),
        status = TAppTaskStatus.OPEN,
        timings = TAppTaskTimings(TAppTaskDate("2022-06-13"), TAppTaskDate("2022-06-27")),
        ownerId = TAppUserId("1"),
        permissionsClient = mutableSetOf(TAppTaskPermissionsClient.READ),
    )

    fun getModels() = listOf(
        stubReady(),
        someAnotherStub()
    )

    fun getModel(model: (TAppTask.() -> Unit)? = null) = model?.let {
        stubReady().apply(it)
    } ?: stubReady()

    fun TAppTask.update(updateableAd: TAppTask) = apply {
        title = updateableAd.title
        description = updateableAd.description
        ownerId = updateableAd.ownerId
        permissionsClient.addAll(updateableAd.permissionsClient)
    }
}
