package ru.ac1d.tasktracker.common.repo.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Timings: IntIdTable("timings") {
    val start = datetime("start")
    val end = datetime("end")
    val estimation = float("estimation")
}
