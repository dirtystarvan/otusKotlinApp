plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":tasktracker-transport-main-openapi"))
    implementation(project(":tasktracker-common"))

    testImplementation(kotlin("test-junit"))
}
