plugins {
    id("dev.tocraft.modmaster.common")
}

val clothConfigVersion: String = parent!!.properties["cloth_config_version"] as String

repositories {
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.shedaniel.me/")
}

dependencies {
    // mixin extras
    implementation(annotationProcessor("io.github.llamalad7:mixinextras-common:${rootProject.properties["mixinextras_version"]}")!!)
    // Cloth Config
    modCompileOnly("me.shedaniel.cloth:cloth-config:${clothConfigVersion}") {
        exclude("net.fabricmc.fabric-api")
    }
}