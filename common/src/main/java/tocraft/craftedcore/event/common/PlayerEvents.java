package tocraft.craftedcore.event.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.level.ServerPlayer;
import tocraft.craftedcore.event.Event;
import tocraft.craftedcore.event.EventFactory;

@SuppressWarnings("unused")
public final class PlayerEvents {
    public static final Event<PlayerJoin> PLAYER_JOIN = EventFactory.createWithVoid();
    public static final Event<PlayerQuit> PLAYER_QUIT = EventFactory.createWithVoid();
    /**
     * Called when the player is recreated
     */
    public static final Event<PlayerRespawn> PLAYER_RESPAWN = EventFactory.createWithVoid();
    public static final Event<AwardAdvancement> AWARD_ADVANCEMENT = EventFactory.createWithVoid();
    public static final Event<RevokeAdvancement> REVOKE_ADVANCEMENT = EventFactory.createWithVoid();

    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    public interface PlayerJoin {
        void join(ServerPlayer player);
    }

    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    public interface PlayerQuit {
        void quit(ServerPlayer player);
    }

    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    public interface PlayerRespawn {
        void clone(ServerPlayer oldPlayer, ServerPlayer newPlayer);
    }

    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    public interface AwardAdvancement {
        void award(ServerPlayer player, Advancement advancement, String criterionKey);
    }

    @FunctionalInterface
    public interface RevokeAdvancement {
        void revoke(ServerPlayer player, Advancement advancement, String criterionKey);
    }
}
