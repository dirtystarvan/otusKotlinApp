import ru.ac1d.tasktracker.common.models.*

object TaskCreateExample {
    val TASK_CREATE_EXAMPLE: TAppTask
        get() = TAppTask(
            id = TAppTaskId("111"),
            title = "Test title",
            description = "Test description",
            type = TAppTaskType.TASK,
            reporterId = TAppUserId("111"),
            executorId = TAppUserId("666"),
            status = TAppTaskStatus.OPEN,
            timings = TAppTaskTimings(
                start = TAppTaskDateImpl("2022-06-20"),
                end = TAppTaskDateImpl("2022-07-04"),
                estimation = 0.5f
            ),
            ownerId = TAppUserId("111"),
            permissionsClient = mutableSetOf(
                TAppTaskPermissionsClient.READ,
                TAppTaskPermissionsClient.UPDATE,
                TAppTaskPermissionsClient.DELETE,
            )
        )
}
