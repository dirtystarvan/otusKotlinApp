import ru.ac1d.tasktracker.common.repo.ITaskRepo

class RepoInMemoryCreateTest: RepoTaskCreateTest() {
    override val repo: ITaskRepo = TaskRepoInMemory()
}