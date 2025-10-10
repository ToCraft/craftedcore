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

val modMenuVersion: String? = parent!!.properties["modmenu_version"] as String

val clothConfigVersion: String = parent!!.properties["cloth_config_version"] as String

repositories {
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.shedaniel.me/")
}

dependencies {
    // mixin extras
    include(implementation(annotationProcessor("io.github.llamalad7:mixinextras-fabric:${rootProject.properties["mixinextras_version"]}")!!)!!)
    if (modMenuVersion != null) {
        modRuntimeOnly("com.terraformersmc:modmenu:${modMenuVersion}") {
            isTransitive = false
        }
    }
    // Cloth Config
    modRuntimeOnly("me.shedaniel.cloth:cloth-config-fabric:${clothConfigVersion}") {
        exclude("net.fabricmc.fabric-api")
    }
}
