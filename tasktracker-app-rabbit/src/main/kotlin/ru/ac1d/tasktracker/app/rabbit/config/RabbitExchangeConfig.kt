package ru.ac1d.tasktracker.app.rabbit.config

data class RabbitExchangeConfig(
    val keyIn: String,
    val keyOut: String,
    val exchange: String,
    val queue: String,
    val consumerTag: String,
    val exchangeType: String = "direct"
)
