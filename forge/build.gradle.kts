plugins {
    id("dev.tocraft.modmaster.forge")
}

tasks.withType<ProcessResources> {
    @Suppress("UNCHECKED_CAST")val modMeta = parent!!.ext["mod_meta"]!! as Map<String, Any>

    filesMatching("META-INF/mods.toml") {
        expand(modMeta)
    }

    outputs.upToDateWhen { false }
}

loom {
    forge {
        mixinConfigs.add("craftedcore.mixins.json")
        if ("${parent!!.name}" == "1.18.2") {
            mixinConfigs.add("craftedcore-forge.mixins.json")
        }
    }
}

dependencies {
    // mixin extras
    compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:${rootProject.properties["mixinextras_version"]}")!!)
    implementation(include("io.github.llamalad7:mixinextras-forge:${rootProject.properties["mixinextras_version"]}")!!)
}