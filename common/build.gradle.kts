plugins {
    id("dev.tocraft.modmaster.common")
}

val clothConfigVersion: String? = parent!!.properties["cloth_config_version"] as String?

dependencies {
    compileOnly("org.spongepowered:mixin:0.8.5")
    compileOnly("io.github.llamalad7:mixinextras-common:${property("mixinextras_version")}")
    annotationProcessor("io.github.llamalad7:mixinextras-common:${property("mixinextras_version")}")
    if (clothConfigVersion != null) {
        compileOnly("me.shedaniel.cloth:cloth-config:${clothConfigVersion}") {
            exclude("net.fabricmc.fabric-api")
        }
    }
}