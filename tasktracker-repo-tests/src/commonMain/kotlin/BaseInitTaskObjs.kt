package ru.ac1d.tasktracker.common.repo.test


import ru.ac1d.tasktracker.common.models.*


abstract class BaseInitTaskObjs(private val operation: String) {
    fun initTestModel(
        suffix: String = "",
        owner: TAppUserId = TAppUserId("1"),
        reporter: TAppUserId = TAppUserId("123"),
        executor: TAppUserId = TAppUserId("456"),
        taskType: TAppTaskType = TAppTaskType.TASK,
        status: TAppTaskStatus = TAppTaskStatus.OPEN,
    ) = TAppTask(
        id = TAppTaskId("repo-$operation-$suffix"),
        title = "$suffix stub",
        description = "$suffix stub description",
        ownerId = owner,
        type = taskType,
        reporterId = reporter,
        executorId = executor,
        status = status,
        //TODO lock
    )
}