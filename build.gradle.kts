plugins {
    id("dev.tocraft.modmaster.root") version ("single-1.7")
}

val clothConfigVersion: String? = properties["cloth_config_version"] as String

ext {
    val modMeta = mutableMapOf<String, Any>()
    modMeta["minecraft"] = project.properties["minecraft"] as String
    modMeta["version"] = version
    if (clothConfigVersion != null) {
        modMeta["clothConfig"] = clothConfigVersion
    }
    set("mod_meta", modMeta)
}
