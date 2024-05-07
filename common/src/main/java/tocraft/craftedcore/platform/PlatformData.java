package tocraft.craftedcore.platform;

import dev.architectury.platform.Mod;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import org.jetbrains.annotations.Nullable;

import java.lang.module.ModuleDescriptor.Version;
import java.nio.file.Path;

@SuppressWarnings("unused")
public final class PlatformData {
    public static boolean isModLoaded(String modid) {
        return Platform.isModLoaded(modid);
    }

    @Nullable
    public static Version getModVersion(String modid) {
        Mod mod = Platform.getMod(modid);
        return mod != null ? Version.parse(mod.getVersion()) : null;
    }

    public static boolean isDevEnv() {
        return Platform.isDevelopmentEnvironment();
    }

    public static EnvType getEnv() {
        return Platform.getEnv();
    }

    public static Path getConfigPath() {
        return Platform.getConfigFolder();
    }

    public static ModLoader getModLoaderId() {
        if (Platform.isFabric()) {
            return ModLoader.FABRIC;
        } else if (Platform.isForge()) {
            return ModLoader.FORGE;
        } else {
            return ModLoader.OTHER;
        }
    }

    public enum ModLoader {
        FABRIC, FORGE, NEOFORGE, OTHER
    }
}
