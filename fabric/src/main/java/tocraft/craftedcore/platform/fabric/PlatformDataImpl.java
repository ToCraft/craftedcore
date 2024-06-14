package tocraft.craftedcore.platform.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;
import tocraft.craftedcore.platform.PlatformData;

import java.lang.module.ModuleDescriptor.Version;
import java.nio.file.Path;

@SuppressWarnings({"unused", "SameReturnValue"})
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

    public static PlatformData.ModLoader getModLoaderId() {
        return PlatformData.ModLoader.FABRIC;
    }
}
