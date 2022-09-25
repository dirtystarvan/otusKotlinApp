import ru.ac1d.tasktracker.common.repo.test.RepoTaskReadTest

class RepoPostgresReadTest: RepoTaskReadTest() {
    override val repo = SqlTestCompanion.repoUnderTestContainer(initObjects)
}