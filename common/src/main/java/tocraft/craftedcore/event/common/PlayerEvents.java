package tocraft.craftedcore.event.common;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
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
    public static final Event<AllowSleepTime> ALLOW_SLEEP_TIME = EventFactory.createWithVoid();
    public static final Event<SleepFinishedTime> SLEEP_FINISHED_TIME = EventFactory.createWithCallback(callbacks -> (level, newTime) -> {
        long newNewTime = 0;
        for (SleepFinishedTime callback : callbacks) {
            long newTimeIn = callback.setTimeAddition(level, newTime);
            if (level.getDayTime() <= newTimeIn) {
                newNewTime = newTimeIn;
            }
        }
        return newNewTime;
    });

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
        void award(ServerPlayer player, AdvancementHolder advancement, String criterionKey);
    }

    @FunctionalInterface
    public interface RevokeAdvancement {
        void revoke(ServerPlayer player, AdvancementHolder advancement, String criterionKey);
    }

    @FunctionalInterface
    public interface AllowSleepTime {
        InteractionResult allowSleepTime(Player player, @Nullable BlockPos sleepingPos, boolean vanillaResult);
    }

    @FunctionalInterface
    public interface SleepFinishedTime {
        long setTimeAddition(ServerLevel level, long newTime);
    }
}
