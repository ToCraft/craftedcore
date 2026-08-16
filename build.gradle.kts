plugins {
    id("dev.tocraft.modmaster.root") version ("2.5-SNAPSHOT")
}

// build.gradle.kts
tasks.register("showExactPluginVersion") {
    doLast {
        println("RUNS")
        buildscript.configurations.findByName("classpath")?.resolvedConfiguration?.resolvedArtifacts?.forEach { artifact ->
            if (artifact.moduleVersion.id.name.contains("modmaster")) {
                println("--- Plugin Info ---")
                println("Declared Version: ${artifact.moduleVersion.id.version}")
                println("Exact File Name:  ${artifact.file.name}")
                println("-------------------")
            }
        }
    }
}
subprojects {
    repositories {
        maven("https://maven.fabricmc.net/") // fabric api
        maven("https://maven.terraformersmc.com/releases/") // mod menu mod
        maven("https://maven.shedaniel.me/") // cloth config
        maven {
            name = "Minecraft Libraries"
            url = uri("https://libraries.minecraft.net")
        }
    }
}
