package tocraft.craftedcore.events.common.neoforge;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import tocraft.craftedcore.events.Event.Result;
import tocraft.craftedcore.events.common.CommandEvents;
import tocraft.craftedcore.events.common.PlayerEvents;

public class NeoForgeEventHandler {
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void event(PlayerLoggedInEvent event) {
		PlayerEvents.PLAYER_JOIN.invoker().join((ServerPlayer) event.getEntity());
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(RegisterCommandsEvent event) {
        CommandEvents.REGISTRATION.invoker().register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }
	
	@SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventPlayerInteractEvent(PlayerInteractEvent.EntityInteract event) {
        Result result = PlayerEvents.INTERACT_ENTITY.invoker().interact(event.getEntity(), event.getTarget(), event.getHand());
        if (result.isPresent()) {
            event.setCanceled(true);
            event.setCancellationResult(result.asMinecraft());
        }
    }
	
	@SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerLoggedOutEvent event) {
        PlayerEvents.PLAYER_QUIT.invoker().quit((ServerPlayer) event.getEntity());
    }
}
