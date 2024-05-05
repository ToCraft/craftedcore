package tocraft.craftedcore.event.common;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.level.ServerPlayer;
import tocraft.craftedcore.event.Event;
import tocraft.craftedcore.event.EventFactory;

@SuppressWarnings("unused")
public class PlayerEvents {
    public static final Event<PlayerJoin> PLAYER_JOIN = EventFactory.createWithVoid();
    public static final Event<PlayerQuit> PLAYER_QUIT = EventFactory.createWithVoid();
    /**
     * Called when the player is recreated
     */
    public static final Event<PlayerRespawn> PLAYER_RESPAWN = EventFactory.createWithVoid();
    public static final Event<AwardAdvancement> AWARD_ADVANCEMENT = EventFactory.createWithVoid();
    public static final Event<RevokeAdvancement> REVOKE_ADVANCEMENT = EventFactory.createWithVoid();

    @FunctionalInterface
    public interface PlayerJoin {
        void join(ServerPlayer player);
    }

    @FunctionalInterface
    public interface PlayerQuit {
        void quit(ServerPlayer player);
    }

    @FunctionalInterface
    public interface PlayerRespawn {
        void clone(ServerPlayer oldPlayer, ServerPlayer newPlayer);
    }

    @FunctionalInterface
    public interface AwardAdvancement {
        void award(AdvancementHolder advancement, String criterionKey);
    }

    @FunctionalInterface
    public interface RevokeAdvancement {
        void revoke(AdvancementHolder advancement, String criterionKey);
    }
}
