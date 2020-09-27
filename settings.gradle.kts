pluginManagement {
    val kotlinVersion: String by settings
    val dokkaPluginVersion: String by settings
    val ktlintVersion: String by settings
    val bintrayPluginVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
        `maven-publish`
        id("org.jetbrains.dokka") version dokkaPluginVersion
        id("org.jlleitschuh.gradle.ktlint") version ktlintVersion
        id("com.jfrog.bintray") version bintrayPluginVersion
    }
}

rootProject.name = "cassandra-unit-spring"
