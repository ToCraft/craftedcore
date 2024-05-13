package tocraft.craftedcore.platform.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

import java.lang.module.ModuleDescriptor.Version;
import java.nio.file.Path;

@SuppressWarnings("unused")
public final class PlatformDataImpl {
    public static boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    @Nullable
    public static Version getModVersion(String modid) {
        return FabricLoader.getInstance().getModContainer(modid).map(modContainer -> Version.parse(modContainer.getMetadata().getVersion().getFriendlyString())).orElse(null);
    }

    public static boolean isDevEnv() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static EnvType getEnv() {
        return FabricLoader.getInstance().getEnvironmentType();
    }

    public static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static ModLoader getModLoaderId() {
        return ModLoader.FABRIC;
    }

    public enum ModLoader {
        FABRIC, FORGE, NEOFORGE, OTHER
    }
}
