package tocraft.craftedcore.events.common;

import net.minecraft.server.level.ServerPlayer;
import tocraft.craftedcore.events.Event;
import tocraft.craftedcore.events.EventBuilder;

public interface PlayerEvents {
	
	 Event<PlayerJoin> PLAYER_JOIN = EventBuilder.createLoop();

	interface PlayerJoin {
        /**
         * Invoked after a player logged into a server level
         *
         * @param player The joined player.
         */
        void join(ServerPlayer player);
    }
}
