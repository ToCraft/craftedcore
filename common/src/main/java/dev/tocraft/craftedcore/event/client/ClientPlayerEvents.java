package dev.tocraft.craftedcore.event.client;

import dev.tocraft.craftedcore.event.Event;
import dev.tocraft.craftedcore.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class ClientPlayerEvents {
    public static final Event<ClientPlayerJoin> CLIENT_PLAYER_JOIN = EventFactory.createWithVoid();
    public static final Event<ClientPlayerQuit> CLIENT_PLAYER_QUIT = EventFactory.createWithVoid();
    /**
     * Called when the player is recreated
     */
    public static final Event<ClientPlayerRespawn> CLIENT_PLAYER_RESPAWN = EventFactory.createWithVoid();

    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    public interface ClientPlayerJoin {
        void join(LocalPlayer player);
    }

    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    public interface ClientPlayerQuit {
        void quit(LocalPlayer player);
    }

    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    public interface ClientPlayerRespawn {
        void respawn(LocalPlayer oldPlayer, LocalPlayer newPlayer);
    }
}
