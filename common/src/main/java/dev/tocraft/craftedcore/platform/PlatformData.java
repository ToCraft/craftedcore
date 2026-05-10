package dev.tocraft.craftedcore.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.lang.module.ModuleDescriptor.Version;
import java.nio.file.Path;
import java.util.ServiceLoader;

@SuppressWarnings({"unused", "Contract"})
public final class PlatformData {
    private static final PlatformDataService SERVICE =
            ServiceLoader.load(PlatformDataService.class).findFirst().orElseThrow();

    public static boolean isModLoaded(String modid) {
        return SERVICE.isModLoaded(modid);
    }

    @Nullable
    public static Version getModVersion(String modid) {
        return SERVICE.getModVersion(modid);
    }

    public static boolean isDevEnv() {
        return SERVICE.isDevEnv();
    }

    @Contract(value = " -> !null", pure = true)
    public static Env getEnv() {
        return SERVICE.getEnv();
    }

    @Contract(value = " -> !null", pure = true)
    public static Path getConfigPath() {
        return SERVICE.getConfigPath();
    }

    @Contract(value = " -> !null", pure = true)
    public static ModLoader getModLoaderId() {
        return SERVICE.getModLoaderId();
    }

    @ApiStatus.Internal
    @Environment(EnvType.CLIENT)
    public static void registerConfigScreen(String name) {
        SERVICE.registerConfigScreen(name);
    }

    public enum ModLoader {
        FABRIC, FORGE, NEOFORGE, OTHER
    }

    public enum Env {
        CLIENT, SERVER;

        public boolean isClient() {
            return this == CLIENT;
        }
    }
}
