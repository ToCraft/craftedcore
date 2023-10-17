package tocraft.craftedcore.fabric;

import dev.architectury.event.events.client.ClientGuiEvent;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class CraftedCoreFabricClient {
	
	public CraftedCoreFabricClient() {
		ClientTickEvents.START_CLIENT_TICK.register(instance -> tocraft.craftedcore.events.client.ClientTickEvents.CLIENT_PRE.invoker().tick(instance));
        ClientTickEvents.END_CLIENT_TICK.register(instance -> tocraft.craftedcore.events.client.ClientTickEvents.CLIENT_POST.invoker().tick(instance));
        
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> ClientGuiEvent.RENDER_HUD.invoker().renderHud(matrices, tickDelta));
	}
}
