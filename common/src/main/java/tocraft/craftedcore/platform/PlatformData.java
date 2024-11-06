package tocraft.craftedcore.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import tocraft.craftedcore.config.Config;

import java.lang.module.ModuleDescriptor.Version;
import java.nio.file.Path;

@SuppressWarnings({"unused", "Contract"})
public final class PlatformData {
    @ExpectPlatform
    public static boolean isModLoaded(String modid) {
        throw new AssertionError();
    }

    @ExpectPlatform
    @Nullable
    public static Version getModVersion(String modid) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isDevEnv() {
        throw new AssertionError();
    }

    @Contract(value = " -> !null", pure = true)
    @ExpectPlatform
    public static EnvType getEnv() {
        throw new AssertionError();
    }

    @ExpectPlatform
    @Contract(value = " -> !null", pure = true)
    public static Path getConfigPath() {
        throw new AssertionError();
    }

    @ExpectPlatform
    @Contract(value = " -> !null", pure = true)
    public static ModLoader getModLoaderId() {
        throw new AssertionError();
    }

    @ExpectPlatform
    @ApiStatus.Internal
    @Environment(EnvType.CLIENT)
    public static void registerConfigScreen(String name) {
        throw new AssertionError();
    }

    public enum ModLoader {
        FABRIC, FORGE, NEOFORGE, OTHER
    }
}
