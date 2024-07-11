buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.diluv.schoomp:Schoomp:1.2.6")
    }
}

plugins {
    id("architectury-plugin") version "3.4-SNAPSHOT" apply true
    id("dev.architectury.loom") version "1.7-SNAPSHOT" apply false
    id("dev.tocraft.preprocessor") version "0.51-SNAPSHOT" apply false
}

allprojects {
    repositories {
        // Add repositories to retrieve artifacts from in here.
        // You should only use this when depending on other mods because
        // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
        // See https://docs.gradle.org/current/userguide/declaring_repositories.html
        // for more information about repositories.
        maven("https://maven.parchmentmc.org")
        maven("https://maven.neoforged.net/releases/")
    }
}

