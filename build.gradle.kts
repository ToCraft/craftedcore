import java.util.*

plugins {
    id("dev.tocraft.modmaster.version")
}

val clothConfigVersion: String? = (ext.get("props") as Properties).getProperty("cloth_config_version")

ext {
    val modMeta = mutableMapOf<String, Any>()
    modMeta["minecraft"] = project.name
    modMeta["version"] = version
    if (clothConfigVersion != null) {
        modMeta["clothConfig"] = clothConfigVersion
    }
    set("mod_meta", modMeta)
}
