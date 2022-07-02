package ru.ac1d.tasktracker.app

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.plugin.koin
import org.slf4j.event.Level
import ru.ac1d.tasktracker.app.api.api
import ru.ac1d.tasktracker.app.koins.taskServiceKoin

fun main(args: Array<String>) = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    install(CORS) {
        allowHeader(HttpHeaders.Accept)
        anyHost()
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            writerWithDefaultPrettyPrinter()

            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }

    install(CallLogging) {
        level = Level.INFO
    }

    koin {
        modules(taskServiceKoin)
    }

    routing {
        get("/help") {
            call.respondText("Help is here!")
        }

        api()
    }
}