import java.util.*

plugins {
    id("dev.tocraft.modmaster.neoforge")
}

dependencies {
    compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:${property("mixinextras_version")}")!!)
    implementation(jarJar("io.github.llamalad7:mixinextras-neoforge:${property("mixinextras_version")}")!!)

    val clothConfigVersion = properties["cloth_config_version"] as String?
    if (clothConfigVersion != null) {
        compileOnly("me.shedaniel.cloth:cloth-config-neoforge:${clothConfigVersion}")
        runtimeOnly("me.shedaniel.cloth:cloth-config-neoforge:${clothConfigVersion}")
    }
}

tasks.processResources {
    from("commonResources")
    val mcVersion = project.property("minecraft")
    val clothVersion = project.property("cloth_config_version")
    filesMatching(listOf("META-INF/neoforge.mods.toml", "META-INF/mods.toml")) {
        expand(mapOf(
            "version" to project.version,
            "minecraft" to mcVersion,
            "clothConfig" to clothVersion
        ))
    }
    outputs.upToDateWhen { false }
}
