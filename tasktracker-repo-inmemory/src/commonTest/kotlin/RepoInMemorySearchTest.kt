import ru.ac1d.tasktracker.common.repo.test.RepoTaskSearchTest

class RepoInMemorySearchTest: RepoTaskSearchTest() {
    override val repo = TaskRepoInMemory(initObjects = initObjects)
}