package tocraft.craftedcore.platform.neoforge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tocraft.craftedcore.config.ConfigLoader;
import tocraft.craftedcore.platform.PlatformData;

import java.lang.module.ModuleDescriptor.Version;
import java.nio.file.Path;
import java.util.Objects;

//#if MC>=1206
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
//#else
//$$ import net.neoforged.neoforge.client.ConfigScreenHandler;
//#endif

@SuppressWarnings({"unused", "SameReturnValue"})
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

    public static PlatformData.ModLoader getModLoaderId() {
        return PlatformData.ModLoader.NEOFORGE;
    }

    @ApiStatus.Internal
    @OnlyIn(Dist.CLIENT)
    public static void registerConfigScreen(String name) {
        if (ModList.get().getModContainerById("cloth_config").isPresent()) {
            //#if MC>=1206
            ModList.get().getModContainerById(name).ifPresent(mod -> mod.registerExtensionPoint(IConfigScreenFactory.class, (minecraft, parent) -> Objects.requireNonNull(ConfigLoader.getConfigByName(name)).constructConfigScreen(parent)));
            //#else
            //$$ ModList.get().getModContainerById(name).ifPresent(mod -> mod.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parent) -> Objects.requireNonNull(ConfigLoader.getConfigByName(name)).constructConfigScreen(parent))));
            //#endif
        }
    }
}
