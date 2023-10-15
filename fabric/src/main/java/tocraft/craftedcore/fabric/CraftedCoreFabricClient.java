package tocraft.craftedcore.fabric;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class CraftedCoreFabricClient {
	
	public CraftedCoreFabricClient() {
		ClientTickEvents.START_CLIENT_TICK.register(instance -> tocraft.craftedcore.events.client.ClientTickEvents.CLIENT_PRE.invoker().tick(instance));
        ClientTickEvents.END_CLIENT_TICK.register(instance -> tocraft.craftedcore.events.client.ClientTickEvents.CLIENT_POST.invoker().tick(instance));
	}
}
