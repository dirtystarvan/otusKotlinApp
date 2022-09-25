package ru.ac1d.tasktracker.common.repo.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object Timings: IntIdTable("timings") {
    val start = varchar("start", 128)
    val end = varchar("end", 128)
    val estimation = float("estimation")
}
