plugins {
    id("dev.tocraft.modmaster.fabric")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft")}")
    implementation("net.fabricmc:fabric-loader:${property("fabric_loader")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${property("fabric")}")

    commonJava(project(":common", "commonJava"))
    commonResources(project(":common", "commonResources"))

    // MixinExtras bundled with the mod
    include(implementation(annotationProcessor("io.github.llamalad7:mixinextras-fabric:${property("mixinextras_version")}")!!)!!)

    val modMenuVersion = properties["modmenu_version"] as String?
    if (modMenuVersion != null) {
        compileOnly("com.terraformersmc:modmenu:${modMenuVersion}") { isTransitive = false }
        runtimeOnly("com.terraformersmc:modmenu:${modMenuVersion}") { isTransitive = false }
    }
    val clothConfigVersion = properties["cloth_config_version"] as String?
    if (clothConfigVersion != null) {
        compileOnly("me.shedaniel.cloth:cloth-config-fabric:${clothConfigVersion}") {
            exclude(group = "net.fabricmc.fabric-api")
        }
        runtimeOnly("me.shedaniel.cloth:cloth-config-fabric:${clothConfigVersion}") {
            exclude(group = "net.fabricmc.fabric-api")
        }
    }
}

tasks.processResources {
    from("commonResources")
    val mcVersion = project.property("minecraft")
    val clothVersion = project.property("cloth_config_version")
    filesMatching("fabric.mod.json") {
        expand(mapOf(
            "version" to project.version,
            "minecraft" to mcVersion,
            "clothConfig" to clothVersion
        ))
    }
    outputs.upToDateWhen { false }
}
