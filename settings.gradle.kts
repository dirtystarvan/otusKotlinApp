rootProject.name = "kotlinBackend"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val openapiVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false
        kotlin("multiplatfrom") version kotlinVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
    }
}

include("tasktracker-transport-main-openapi")
include("tasktracker-common")
include("tasktracker-mappers-jvm")
include("tasktracker-app-ktor")
include("tasktracker-services")
include("tasktracker-stubs")
include("tasktracker-biz")
include("tasktracker-app-rabbit")
include("tasktracker-repo-tests")
include("tasktracker-repo-inmemory")
