import org.jetbrains.kotlin.util.suffixIfNot

val ktorVersion: String by project

// ex: Converts to "io.ktor:ktor-ktor-server-netty:2.0.1" with only ktor("netty")
fun DependencyHandler.ktor(module: String, prefix: String = "server-", version: String? = ktorVersion): Any =
    "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("application")
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    val logbackVersion: String by project
    val koinVersion: String by project

    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation(ktor("core")) // "io.ktor:ktor-server-core:$ktorVersion"
    implementation(ktor("netty")) // "io.ktor:ktor-ktor-server-netty:$ktorVersion"

    // jackson
    implementation(ktor("jackson", "serialization")) // io.ktor:ktor-serialization-jackson
    implementation(ktor("content-negotiation")) // io.ktor:ktor-server-content-negotiation

    //Dependency injection
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")

    implementation(ktor("locations"))
    implementation(ktor("caching-headers"))
    implementation(ktor("call-logging"))
    implementation(ktor("auto-head-response"))
    implementation(ktor("cors")) // "io.ktor:ktor-cors:$ktorVersion"
    implementation(ktor("default-headers")) // "io.ktor:ktor-cors:$ktorVersion"
    implementation(ktor("cors")) // "io.ktor:ktor-cors:$ktorVersion"
    implementation(ktor("auto-head-response"))

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // transport models
    implementation(project(":tasktracker-common"))
    implementation(project(":tasktracker-transport-main-openapi"))
    implementation(project(":tasktracker-mappers-jvm"))

    // Services
    implementation(project(":tasktracker-services"))

    // Stubs
    implementation(project(":tasktracker-stubs"))

    testImplementation(kotlin("test-junit"))
    testImplementation(ktor("test-host")) // "io.ktor:ktor-server-test-host:$ktorVersion"
    testImplementation("io.ktor:ktor-client-content-negotiation:2.0.2")
}
