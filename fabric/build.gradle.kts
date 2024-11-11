import java.util.*

plugins {
    id("dev.tocraft.modmaster.fabric")
}

tasks.withType<ProcessResources> {
    @Suppress("UNCHECKED_CAST") val modMeta = parent!!.ext["mod_meta"]!! as Map<String, Any>
    //inputs.properties.putAll(modMeta)

    filesMatching("fabric.mod.json") {
        expand(modMeta)
    }

    outputs.upToDateWhen { false }
}

val modMenuVersion: String? = (parent!!.ext.get("props") as Properties).getProperty("modmenu_version")

val clothConfigVersion: String? = (parent!!.ext.get("props") as Properties).getProperty("cloth_config_version")

repositories {
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.shedaniel.me/")
}

dependencies {
    // mixin extras
    include(implementation(annotationProcessor("io.github.llamalad7:mixinextras-fabric:${rootProject.properties["mixinextras_version"]}")!!)!!)
    if (modMenuVersion != null) {
        modCompileOnly ("com.terraformersmc:modmenu:${modMenuVersion}") {
            isTransitive = false
        }
        modRuntimeOnly ("com.terraformersmc:modmenu:${modMenuVersion}") {
            isTransitive = false
        }
    }
    // Cloth Config
    if (clothConfigVersion != null) {
        modRuntimeOnly("me.shedaniel.cloth:cloth-config-fabric:${clothConfigVersion}") {
            exclude("net.fabricmc.fabric-api")
        }
    }
}
