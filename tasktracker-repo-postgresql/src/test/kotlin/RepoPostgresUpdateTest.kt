import ru.ac1d.tasktracker.common.repo.test.RepoTaskUpdateTest

class RepoPostgresUpdateTest: RepoTaskUpdateTest() {
    override val repo = SqlTestCompanion.repoUnderTestContainer(initObjects)
}