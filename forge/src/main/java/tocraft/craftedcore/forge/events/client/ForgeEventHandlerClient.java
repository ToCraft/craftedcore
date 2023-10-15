package tocraft.craftedcore.forge.events.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tocraft.craftedcore.events.client.ClientPlayerEvents;

@OnlyIn(Dist.CLIENT)
public class ForgeEventHandlerClient {
	@SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ClientPlayerNetworkEvent.LoggingIn event) {
        ClientPlayerEvents.CLIENT_PLAYER_JOIN.invoker().join(event.getPlayer());
    }
}
