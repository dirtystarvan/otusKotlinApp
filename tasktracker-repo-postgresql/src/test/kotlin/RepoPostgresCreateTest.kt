import ru.ac1d.tasktracker.common.repo.ITaskRepo
import ru.ac1d.tasktracker.common.repo.test.RepoTaskCreateTest

class RepoPostgresCreateTest: RepoTaskCreateTest() {
    override val repo: ITaskRepo = SqlTestCompanion.repoUnderTestContainer()
}