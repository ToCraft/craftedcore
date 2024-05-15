package tocraft.craftedcore.platform.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.Nullable;

import java.lang.module.ModuleDescriptor.Version;
import java.nio.file.Path;

@SuppressWarnings("unused")
public final class PlatformDataImpl {
    public static boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    @Nullable
    public static Version getModVersion(String modid) {
        return ModList.get().getModContainerById(modid).map(modContainer -> Version.parse(modContainer.getModInfo().getVersion().toString())).orElse(null);
    }

    public static boolean isDevEnv() {
        return !FMLLoader.isProduction();
    }

    public static Dist getEnv() {
        return FMLLoader.getDist();
    }

    public static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static ModLoader getModLoaderId() {
        return ModLoader.FORGE;
    }

    public enum ModLoader {
        FABRIC, FORGE, NEOFORGE, OTHER
    }
}
