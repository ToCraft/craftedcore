plugins {
    id("dev.tocraft.modmaster.common")
}

loom {
    accessWidenerPath = file("../../../common/src/main/resources/craftedcore.accessWidener")
}

dependencies {
    // mixin extras
    implementation(annotationProcessor("io.github.llamalad7:mixinextras-common:${rootProject.properties["mixinextras_version"]}")!!)
}