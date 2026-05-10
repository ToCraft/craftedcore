package dev.tocraft.craftedcore.platform.neoforge;

import dev.tocraft.craftedcore.config.Config;
import dev.tocraft.craftedcore.config.ConfigLoader;
import dev.tocraft.craftedcore.platform.PlatformData;
import dev.tocraft.craftedcore.platform.PlatformDataService;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.lang.module.ModuleDescriptor.Version;
import java.nio.file.Path;

@SuppressWarnings({"unused", "SameReturnValue"})
@ApiStatus.Internal
public final class PlatformDataImpl implements PlatformDataService {
    @Override
    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    @Override
    @Nullable
    public Version getModVersion(String modid) {
        return ModList.get().getModContainerById(modid).map(modContainer -> Version.parse(modContainer.getModInfo().getVersion().toString())).orElse(null);
    }

    @Override
    public boolean isDevEnv() {
        return !FMLLoader.getCurrent().isProduction();
    }

    @Override
    public PlatformData.Env getEnv() {
        return FMLEnvironment.getDist().isClient() ? PlatformData.Env.CLIENT : PlatformData.Env.SERVER;
    }

    @Override
    public Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public PlatformData.ModLoader getModLoaderId() {
        return PlatformData.ModLoader.NEOFORGE;
    }

    @Override
    @ApiStatus.Internal
    @OnlyIn(Dist.CLIENT)
    public void registerConfigScreen(String name) {
        if (ModList.get().getModContainerById("cloth_config").isPresent()) {
            ModList.get().getModContainerById(name).ifPresent(mod -> mod.registerExtensionPoint(IConfigScreenFactory.class, (minecraft, parent) -> {
                Config c;
                Screen s;
                return (c = ConfigLoader.getConfigByName(name)) != null && ((s = c.constructConfigScreen(parent)) != null) ? s : parent;
            }));
        }
    }
}
