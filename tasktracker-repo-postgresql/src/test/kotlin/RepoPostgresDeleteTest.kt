import ru.ac1d.tasktracker.common.repo.ITaskRepo
import ru.ac1d.tasktracker.common.repo.test.RepoTaskDeleteTest

class RepoPostgresDeleteTest: RepoTaskDeleteTest() {
    override val repo: ITaskRepo = SqlTestCompanion.repoUnderTestContainer(initObjects)
}