plugins {
    id("dev.tocraft.modmaster.testmod-fabric")
}

dependencies {
    implementation("net.fabricmc.fabric-api:fabric-api:${property("fabric")}")
}