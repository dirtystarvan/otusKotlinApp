import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.Test
import kotlin.test.BeforeTest
import org.testcontainers.containers.RabbitMQContainer
import ru.ac1d.api.v1.models.*
import ru.ac1d.tasktracker.app.rabbit.RabbitController
import ru.ac1d.tasktracker.app.rabbit.config.RabbitConnectConfig
import ru.ac1d.tasktracker.app.rabbit.config.RabbitExchangeConfig
import ru.ac1d.tasktracker.app.rabbit.processor.RabbitDirectProcessor
import kotlin.test.assertEquals

class RabbitTestContainer {
    companion object {
        const val exchange = "test-exchange"
        const val exchangeType = "direct"
    }

    private val container by lazy {
        RabbitMQContainer("rabbitmq:latest").apply {
            withExposedPorts(5672, 15672)
            withUser("guest", "guest")
            start()
        }
    }

    private val rabbitMqTestPort: Int by lazy {
        container.getMappedPort(5672)
    }

    private val config by lazy {
        RabbitConnectConfig(
            port = rabbitMqTestPort
        )
    }

    private val service = TaskService()
    private val processor by lazy {
        RabbitDirectProcessor(
            config = config,
            processorConfig = RabbitExchangeConfig(
                keyIn = "in",
                keyOut = "out",
                exchange = exchange,
                queue = "queue",
                consumerTag = "test-tag",
                exchangeType = exchangeType
            ),
            service = service
        )
    }

    private val controller by lazy {
        RabbitController(
            processors = setOf(processor)
        )
    }
    private val mapper = ObjectMapper()

    @BeforeTest
    fun tearUp() {
        controller.start()
    }

    @Test
    fun adCreateTest() {
        val keyOut = processor.processorConfig.keyOut
        val keyIn = processor.processorConfig.keyIn
        ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")

                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)

                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }

                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, mapper.writeValueAsBytes(createTask))

                runBlocking {
                    withTimeoutOrNull(265L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = mapper.readValue(responseJson, TaskCreateResponse::class.java)
                val expected = BizTaskStub.get()

                assertEquals(expected.title, response.task?.baseTask?.title)
                assertEquals(expected.description, response.task?.baseTask?.description)
            }
        }
    }

    private val createTask = with(BizTaskStub.get()) {
        TaskCreateRequest(
            task = TaskCreateObject(
                baseTask = TaskUpdatable(
                    title = title,
                    description = description
                )
            ),
            requestType = "create",
            debug = TaskDebug(
                mode = TaskRequestDebugMode.STUB,
                stub = TaskRequestDebugStubs.SUCCESS
            )
        )
    }
}
