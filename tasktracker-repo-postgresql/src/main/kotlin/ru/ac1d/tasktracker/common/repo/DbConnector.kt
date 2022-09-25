package ru.ac1d.tasktracker.common.repo

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.DEFAULT_ISOLATION_LEVEL
import org.jetbrains.exposed.sql.transactions.transaction

class DbConnector(
    private val url: String,
    private val user: String,
    private val password: String,
    private val schema: String
) {
    private val databaseConfig: DatabaseConfig = DatabaseConfig { defaultIsolationLevel = DEFAULT_ISOLATION_LEVEL }

    companion object {
        private const val postgresDriver = "org.postgresql.Driver"
    }

    private val globalConnection = Database.connect(url, postgresDriver, user, password, databaseConfig = databaseConfig)

    fun connect(vararg tables: Table): Database {
        transaction(globalConnection) {
            connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS $schema", false).executeUpdate()
        }

        val connect = Database.connect(
            url, postgresDriver, user, password,
            databaseConfig = databaseConfig,
            setupConnection = { connection ->
                connection.transactionIsolation = DEFAULT_ISOLATION_LEVEL
                connection.schema = schema
            }
        )

        transaction(connect) {
            if (System.getenv("tapp.sql_drop_db")?.toBoolean() == true) {
                SchemaUtils.drop(*tables, inBatch = true)
                SchemaUtils.create(*tables, inBatch = true)
            } else {
                SchemaUtils.createMissingTablesAndColumns(*tables, inBatch = true)
            }
        }

        return connect
    }
}