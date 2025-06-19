import java.util.*

plugins {
    id("dev.tocraft.modmaster.common")
}

val clothConfigVersion: String? = (parent!!.ext.get("props") as Properties).getProperty("cloth_config_version")

repositories {
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.shedaniel.me/")
}

dependencies {
    // mixin extras
    implementation(annotationProcessor("io.github.llamalad7:mixinextras-common:${rootProject.properties["mixinextras_version"]}")!!)
    // Cloth Config
    if (clothConfigVersion != null) {
        modCompileOnly("me.shedaniel.cloth:cloth-config:${clothConfigVersion}") {
            exclude("net.fabricmc.fabric-api")
        }
    }
}