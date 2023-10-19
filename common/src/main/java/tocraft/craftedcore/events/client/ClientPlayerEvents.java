package tocraft.craftedcore.events.client;

import net.minecraft.client.player.LocalPlayer;
import tocraft.craftedcore.events.Event;
import tocraft.craftedcore.events.EventBuilder;

public interface ClientPlayerEvents {
	
	 Event<ClientPlayerJoin> CLIENT_PLAYER_JOIN = EventBuilder.createLoop();
	 
	 Event<ClientPlayerQuit> CLIENT_PLAYER_QUIT = EventBuilder.createLoop();

	interface ClientPlayerJoin {
        /**
         * Invoked after a player logged into a server level
         *
         * @param player The joined player.
         */
        void join(LocalPlayer player);
    }
	
	interface ClientPlayerQuit {
        /**
         * Invoked after a player logged out of a server level
         *
         * @param player The player who quit.
         */
        void quit(LocalPlayer player);
    }
}
