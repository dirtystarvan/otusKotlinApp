import ru.ac1d.tasktracker.common.repo.test.RepoTaskReadTest

class RepoInMemoryReadTest: RepoTaskReadTest() {
    override val repo = TaskRepoInMemory(initObjects = initObjects)
}