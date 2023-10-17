package tocraft.craftedcore.events.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import tocraft.craftedcore.events.Event;
import tocraft.craftedcore.events.EventBuilder;

public interface PlayerEvents {
	
	 Event<PlayerJoin> PLAYER_JOIN = EventBuilder.createLoop();
	 
	 Event<InteractEntity> INTERACT_ENTITY = EventBuilder.createEventResult();

	interface PlayerJoin {
        /**
         * Invoked after a player logged into a server level
         *
         * @param player The joined player.
         */
        void join(ServerPlayer player);
    }
	
	interface InteractEntity {
        Event.Result interact(Player player, Entity entity, InteractionHand hand);
    }
}
