package tocraft.craftedcore.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.network.ModernNetworking;
import tocraft.craftedcore.platform.PlatformData;

import java.nio.file.Path;

@SuppressWarnings("unused")
public interface Config {
    @Nullable
    String getName();

    @Nullable
    default Path getPath() {
        return getName() != null ? ConfigLoader.getConfigPath(getName()) : null;
    }

    default void save() {
        ConfigLoader.writeConfigFile(this);
    }

    default void sendToPlayer(ServerPlayer target) {
        ModernNetworking.sendToPlayer(target, ConfigLoader.CONFIG_SYNC, ConfigLoader.getConfigSyncTag(this));
    }

    default void sendToAllPlayers(ServerLevel serverLevel) {
        for (ServerPlayer target : serverLevel.players()) {
            sendToPlayer(target);
        }
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    default Screen constructConfigScreen(Screen parent) {
        if (PlatformData.isModLoaded("cloth-config") || PlatformData.isModLoaded("cloth_config")) {
            return ClothConfigApi.constructConfigScreen(this, parent);
        } else {
            CraftedCore.LOGGER.warn("Cloth config not found!");
            return null;
        }
    }
}
