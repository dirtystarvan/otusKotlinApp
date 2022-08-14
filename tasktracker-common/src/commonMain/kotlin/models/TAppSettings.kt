package ru.ac1d.tasktracker.common.models

import ru.ac1d.tasktracker.common.repo.ITaskRepo

data class TAppSettings(
    val repoStub: ITaskRepo = ITaskRepo.NONE,
    val repoTest: ITaskRepo = ITaskRepo.NONE,
    val repoProd: ITaskRepo = ITaskRepo.NONE,
)