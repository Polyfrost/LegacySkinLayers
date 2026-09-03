plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.ploceus)
}

group = "org.polyfrost"
version = libs.versions.skin.layers.get()

repositories {
    mavenCentral()
    maven("https://maven.cloverclient.com/releases")
    maven("https://maven.legacyfabric.net/")
}

dependencies {
    minecraft(libs.minecraft)
    mappings(libs.legacy.yarn)

    modImplementation(libs.devauth.fabric)
    modImplementation(libs.fabric.loader)
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching(listOf("fabric.mod.json")) {
        expand(mapOf("version" to version))
    }
}

kotlin {
    jvmToolchain(25)
}

loom {
    runs.named("client") {
        ideConfigGenerated(true)
        runDir("run")

        jvmArguments.add("-Ddevauth.enabled=true")
        jvmArguments.add("-Ddevauth.account=alt")
    }
}