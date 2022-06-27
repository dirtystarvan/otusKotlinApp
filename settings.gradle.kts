rootProject.name = "kotlinBackend"

pluginManagement {
    plugins {
        val openapiVersion: String by settings

        id("org.openapi.generator") version openapiVersion apply false
    }
}

include("tasktracker-transport-main-openapi")
include("tasktracker-common")
include("tasktracker-mappers-jvm")
