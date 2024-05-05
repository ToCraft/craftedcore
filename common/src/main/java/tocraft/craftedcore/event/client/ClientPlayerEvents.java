package tocraft.craftedcore.event.client;

import dev.architectury.event.events.client.ClientPlayerEvent;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import tocraft.craftedcore.event.Event;
import tocraft.craftedcore.event.EventFactory;

@SuppressWarnings("unused")
public class ClientPlayerEvents {
    public static final Event<ClientPlayerJoin> CLIENT_PLAYER_JOIN = EventFactory.createWithVoid();
    public static final Event<ClientPlayerQuit> CLIENT_PLAYER_QUIT = EventFactory.createWithVoid();
    /**
     * Called when the player is recreated
     */
    public static final Event<ClientPlayerRespawn> CLIENT_PLAYER_RESPAWN = EventFactory.createWithVoid();

    @FunctionalInterface
    public interface ClientPlayerJoin {
        void join(LocalPlayer player);
    }

    @FunctionalInterface
    public interface ClientPlayerQuit {
        void quit(LocalPlayer player);
    }

    @FunctionalInterface
    public interface ClientPlayerRespawn {
        void respawn(LocalPlayer oldPlayer, LocalPlayer newPlayer);
    }
}
