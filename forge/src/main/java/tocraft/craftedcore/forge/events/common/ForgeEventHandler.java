package tocraft.craftedcore.forge.events.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tocraft.craftedcore.events.common.CommandEvents;
import tocraft.craftedcore.events.common.PlayerEvents;

public class ForgeEventHandler {
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void event(PlayerLoggedInEvent event) {
		PlayerEvents.PLAYER_JOIN.invoker().join((ServerPlayer) event.getEntity());
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(RegisterCommandsEvent event) {
        CommandEvents.REGISTRATION.invoker().register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }
}
