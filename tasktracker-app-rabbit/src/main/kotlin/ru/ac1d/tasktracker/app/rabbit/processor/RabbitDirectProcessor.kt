package ru.ac1d.tasktracker.app.rabbit.processor

import TaskService
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import fromTransport
import kotlinx.datetime.Clock
import ru.ac1d.api.v1.models.IRequest
import ru.ac1d.tasktracker.app.rabbit.config.RabbitConnectConfig
import ru.ac1d.tasktracker.app.rabbit.config.RabbitExchangeConfig
import ru.ac1d.tasktracker.common.TrackerAppContext
import ru.ac1d.tasktracker.common.helpers.addError
import ru.ac1d.tasktracker.common.helpers.asTAppError
import ru.ac1d.tasktracker.common.models.TAppStates
import toTransport

class RabbitDirectProcessor(
    config: RabbitConnectConfig,
    processorConfig: RabbitExchangeConfig,
    private val service: TaskService,
) : RabbitProcessorBase(config, processorConfig) {

    private val context = TrackerAppContext()

    override suspend fun Channel.processMessage(message: Delivery) {
        context.apply {
            timeStart = Clock.System.now()
        }

        jacksonMapper.readValue(message.body, IRequest::class.java).run {
            context.fromTransport(this).also {
                println("TYPE: ${this::class.simpleName}")
            }
        }

        val response = service.exec(context).run { context.toTransport() }

        jacksonMapper.writeValueAsBytes(response).also {
            println("Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}")
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
        }.also {
            println("published")
        }
    }

    override fun Channel.onError(e: Throwable) {
        e.printStackTrace()
        context.status = TAppStates.FAILING
        context.addError(error = e.asTAppError())
        val response = context.toTransport()

        jacksonMapper.writeValueAsBytes(response).also {
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
        }
    }
}
