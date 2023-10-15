package tocraft.craftedcore.forge.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tocraft.craftedcore.events.common.PlayerEvents;

public class ForgeEvents {
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void event(PlayerLoggedInEvent event) {
		PlayerEvents.PLAYER_JOIN.invoker().join((ServerPlayer) event.getEntity());
	}	
}
