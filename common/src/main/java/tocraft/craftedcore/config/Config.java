package tocraft.craftedcore.config;

import dev.architectury.networking.NetworkManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;

public interface Config {
    /**
     * This should be overwritten to improve the performance and avoid errors
     */
    @Nullable
    default String getName() {
        List<String> possibleNames = ConfigLoader.getConfigNames(this);
        return !possibleNames.isEmpty() ? possibleNames.get(0) : null;
    }

    @Nullable
    default Path getPath() {
        return getName() != null ? ConfigLoader.getConfigPath(getName()) : null;
    }

    default void save() {
        ConfigLoader.writeConfigFile(this);
    }

    default void sendToPlayer(ServerPlayer target) {
        NetworkManager.sendToPlayer(target, new ConfigLoader.PacketPayload(ConfigLoader.getConfigSyncTag(this)));
    }

    default void sendToAllPlayers(ServerLevel serverLevel) {
        for (ServerPlayer target : serverLevel.players()) {
            sendToPlayer(target);
        }
    }
}
