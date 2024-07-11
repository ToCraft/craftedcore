import kotlin.io.path.name

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.parchmentmc.org")
        maven("https://maven.tocraft.dev/public")
        gradlePluginPortal()
        mavenLocal()
    }
}

rootProject.buildFileName = "root.gradle.kts"

file("props").listFiles()?.forEach {
    val props = it.readLines()
    var foundFabric = false
    for (line in props) {
        if (line.startsWith("fabric")) {
            foundFabric = true
            break
        }
    }
    var foundForge = false
    for (line in props) {
        if (line.startsWith("forge")) {
            foundForge = true
            break
        }
    }
    var foundNeoForge = false
    for (line in props) {
        if (line.startsWith("neoforge")) {
            foundNeoForge = true
            break
        }
    }

    var version = it.name.replace(".properties", "")
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle"
    }

    include(":$version:common")
    project(":$version:common").apply {
        projectDir = file("common")
    }
    include(":$version:testmod-common")
    project(":$version:testmod-common").apply {
        projectDir = file("testmod-common")
    }

    if (foundFabric) {
        include(":$version:fabric")
        project(":$version:fabric").apply {
            projectDir = file("fabric")
        }
        include(":$version:testmod-fabric")
        project(":$version:testmod-fabric").apply {
            projectDir = file("testmod-fabric")
        }
    }
    if (foundForge) {
        include(":$version:forge")
        project(":$version:forge").apply {
            projectDir = file("forge")
        }
        include(":$version:testmod-forge")
        project(":$version:testmod-forge").apply {
            projectDir = file("testmod-forge")
        }
    }
    if (foundNeoForge) {
        include(":$version:neoforge")
        project(":$version:neoforge").apply {
            projectDir = file("neoforge")
        }
        include(":$version:testmod-neoforge")
        project(":$version:testmod-neoforge").apply {
            projectDir = file("testmod-neoforge")
        }
    }
}

rootProject.name = "craftedcore"