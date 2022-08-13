import ru.ac1d.tasktracker.common.repo.test.RepoTaskDeleteTest

class RepoInMemoryDeleteTest: RepoTaskDeleteTest() {
    override val repo = TaskRepoInMemory(initObjects = initObjects)
}