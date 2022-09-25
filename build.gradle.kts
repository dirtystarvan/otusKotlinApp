plugins {
    kotlin("jvm") apply false
}

group = "ru.ac1d"
version = "1.0"

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    tasks.forEach {
        println("TASK: $it ${it::class.simpleName}")
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-opt-in=kotlin.RequiresOptIn")
            jvmTarget = "11"
        }
    }
}
