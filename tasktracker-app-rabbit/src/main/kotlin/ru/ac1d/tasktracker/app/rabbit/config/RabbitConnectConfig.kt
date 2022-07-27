package ru.ac1d.tasktracker.app.rabbit.config

class RabbitConnectConfig(
    val host: String = "localhost",
    val port: Int = 5672,
    val user: String = "guest",
    val password: String = "guest",
)