package tocraft.craftedcore.config;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import tocraft.craftedcore.network.ModernNetworking;

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
}
