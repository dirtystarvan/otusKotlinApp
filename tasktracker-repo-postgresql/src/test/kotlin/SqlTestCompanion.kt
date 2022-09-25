import org.testcontainers.containers.PostgreSQLContainer
import ru.ac1d.tasktracker.common.models.TAppTask
import ru.ac1d.tasktracker.common.repo.TaskRepoPostgres
import java.time.Duration

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.2")

object SqlTestCompanion {
    private const val USER = "postgres"
    private const val PASS = "strongpassword"
    private const val SCHEMA = "tasker_db"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withStartupTimeout(Duration.ofSeconds(300L))
            start()
        }
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repoUnderTestContainer(initObjects: Collection<TAppTask> = emptyList()): TaskRepoPostgres {
        return TaskRepoPostgres(url, USER, PASS, SCHEMA, initObjects)
    }
}
