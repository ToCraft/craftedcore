package tocraft.craftedcore.config;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
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
        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());

        packet.writeNbt(ConfigLoader.getConfigSyncTag(this));
        NetworkManager.sendToPlayer(target, ConfigLoader.CONFIG_SYNC, packet);
    }

    default void sendToAllPlayers(ServerLevel serverLevel) {
        for (ServerPlayer target : serverLevel.players()) {
            sendToPlayer(target);
        }
    }
}
