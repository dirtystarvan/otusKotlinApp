package ru.ac1d.tasktracker.app.rabbit

import TaskService
import ru.ac1d.tasktracker.app.rabbit.config.RabbitConnectConfig
import ru.ac1d.tasktracker.app.rabbit.config.RabbitExchangeConfig
import ru.ac1d.tasktracker.app.rabbit.processor.RabbitDirectProcessor

fun main() {
    val config = RabbitConnectConfig()
    val service = TaskService()

    val producerConfig = RabbitExchangeConfig(
        keyIn = "in",
        keyOut = "out",
        exchange = "transport-exchange",
        queue = "queue",
        consumerTag = "consumer",
        exchangeType = "direct"
    )

    val processor by lazy {
        RabbitDirectProcessor(
            config = config,
            processorConfig = producerConfig,
            service = service
        )
    }

    val controller by lazy {
        RabbitController(
            processors = setOf(processor)
        )
    }

    controller.start()
}
