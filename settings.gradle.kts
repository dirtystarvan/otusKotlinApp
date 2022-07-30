rootProject.name = "kotlinBackend"

pluginManagement {
    plugins {
        val openapiVersion: String by settings
        val kotlinVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false
        kotlin("multiplatfrom") version kotlinVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
    }
}

include("tasktracker-transport-main-openapi")
include("tasktracker-common")
include("tasktracker-mappers-jvm")
