import ru.ac1d.tasktracker.common.repo.test.RepoTaskSearchTest

class RepoPostgresSearchTest: RepoTaskSearchTest() {
    override val repo = SqlTestCompanion.repoUnderTestContainer(initObjects)
}