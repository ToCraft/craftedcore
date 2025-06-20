import java.util.*

plugins {
    id("dev.tocraft.modmaster.neoforge")
}

tasks.withType<ProcessResources> {
    @Suppress("UNCHECKED_CAST") val modMeta = parent!!.ext["mod_meta"]!! as Map<String, Any>

    filesMatching("META-INF/mods.toml") {
        expand(modMeta)
    }

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(modMeta)
    }


    outputs.upToDateWhen { false }
}

val clothConfigVersion: String? = parent!!.properties["cloth_config_version"] as String

repositories {
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.shedaniel.me/")
}

dependencies {
    // mixin extras
    compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:${rootProject.properties["mixinextras_version"]}")!!)
    implementation(include("io.github.llamalad7:mixinextras-neoforge:${rootProject.properties["mixinextras_version"]}")!!)
    // Cloth Config
    if (clothConfigVersion != null) {
        modRuntimeOnly("me.shedaniel.cloth:cloth-config-neoforge:${clothConfigVersion}")
    }
}