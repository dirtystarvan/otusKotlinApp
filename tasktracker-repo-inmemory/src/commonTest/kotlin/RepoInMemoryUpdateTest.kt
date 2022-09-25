import ru.ac1d.tasktracker.common.repo.test.RepoTaskUpdateTest

class RepoInMemoryUpdateTest: RepoTaskUpdateTest() {
    override val repo = TaskRepoInMemory(initObjects = initObjects)
}