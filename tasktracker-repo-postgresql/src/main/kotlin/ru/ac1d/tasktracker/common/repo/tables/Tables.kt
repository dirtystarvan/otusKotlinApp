package ru.ac1d.tasktracker.common.repo.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import java.util.*

open class StringIdTable(name: String = "", columnName: String = "id", columnLength: Int = 50) : IdTable<String>(name) {
    override val id: Column<EntityID<String>> =
        varchar(columnName, columnLength).uniqueIndex().default(generateUuidAsStringFixedSize())
            .entityId()

    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(id) }

    private fun generateUuidAsStringFixedSize() =
        UUID.randomUUID().toString().replace("-", "").substring(0, 9)
}
