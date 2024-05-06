package tocraft.craftedcore.platform;

import dev.architectury.platform.Platform;

@SuppressWarnings("unused")
public final class PlatformData {
    public static boolean isModLoaded(String modid) {
        return Platform.isModLoaded(modid);
    }

    public static boolean isDevEnv() {
        return Platform.isDevelopmentEnvironment();
    }

    public static ModLoader getModLoaderId() {
        if (Platform.isFabric()) {
            return ModLoader.FABRIC;
        } else if (Platform.isMinecraftForge()) {
            return ModLoader.FORGE;
        } else if (Platform.isNeoForge()) {
            return ModLoader.NEOFORGE;
        } else {
            return ModLoader.OTHER;
        }
    }

    public enum ModLoader {
        FABRIC, FORGE, NEOFORGE, OTHER
    }
}
