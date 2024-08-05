plugins {
    id("dev.tocraft.modmaster.version")
}

ext {
    val modMeta = mutableMapOf<String, Any>()
    modMeta["minecraft"] = project.name
    modMeta["version"] = version
    set("mod_meta", modMeta)
}
