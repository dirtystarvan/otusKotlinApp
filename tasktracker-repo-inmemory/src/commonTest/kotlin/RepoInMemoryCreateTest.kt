import ru.ac1d.tasktracker.common.repo.ITaskRepo
import ru.ac1d.tasktracker.common.repo.test.RepoTaskCreateTest

class RepoInMemoryCreateTest: RepoTaskCreateTest() {
    override val repo: ITaskRepo = TaskRepoInMemory()
}