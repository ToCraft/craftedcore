package dev.tocraft.craftedcore.platform.fabric;

import dev.tocraft.craftedcore.fabric.client.CraftedCoreFabricClient;
import dev.tocraft.craftedcore.platform.PlatformData;
import dev.tocraft.craftedcore.platform.PlatformDataService;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.lang.module.ModuleDescriptor.Version;
import java.nio.file.Path;

@SuppressWarnings({"unused", "SameReturnValue"})
public final class PlatformDataImpl implements PlatformDataService {
    @Override
    public boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    @Override
    @Nullable
    public Version getModVersion(String modid) {
        return FabricLoader.getInstance().getModContainer(modid).map(modContainer -> Version.parse(modContainer.getMetadata().getVersion().getFriendlyString())).orElse(null);
    }

    @Override
    public boolean isDevEnv() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public PlatformData.Env getEnv() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT
                ? PlatformData.Env.CLIENT : PlatformData.Env.SERVER;
    }

    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public PlatformData.ModLoader getModLoaderId() {
        return PlatformData.ModLoader.FABRIC;
    }

    @Override
    @ApiStatus.Internal
    @Environment(EnvType.CLIENT)
    public void registerConfigScreen(String name) {
        CraftedCoreFabricClient.CONFIGS.add(name);
    }
}
