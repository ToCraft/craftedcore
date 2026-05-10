package dev.tocraft.craftedcore.platform;

import org.jetbrains.annotations.Nullable;

import java.lang.module.ModuleDescriptor.Version;
import java.nio.file.Path;

public interface PlatformDataService {
    boolean isModLoaded(String modid);

    @Nullable
    Version getModVersion(String modid);

    boolean isDevEnv();

    PlatformData.Env getEnv();

    Path getConfigPath();

    PlatformData.ModLoader getModLoaderId();

    void registerConfigScreen(String name);
}
