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

dependencies {
    // mixin extras
    include(implementation(annotationProcessor("io.github.llamalad7:mixinextras-fabric:${rootProject.properties["mixinextras_version"]}")!!)!!)
}
