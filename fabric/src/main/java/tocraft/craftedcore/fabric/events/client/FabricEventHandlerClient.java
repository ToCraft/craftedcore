package tocraft.craftedcore.fabric.events.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import tocraft.craftedcore.events.client.ClientGuiEvents;

public class FabricEventHandlerClient {
	
	public FabricEventHandlerClient() {
		ClientTickEvents.START_CLIENT_TICK.register(instance -> tocraft.craftedcore.events.client.ClientTickEvents.CLIENT_PRE.invoker().tick(instance));
        ClientTickEvents.END_CLIENT_TICK.register(instance -> tocraft.craftedcore.events.client.ClientTickEvents.CLIENT_POST.invoker().tick(instance));
        
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> ClientGuiEvents.RENDER_HUD.invoker().renderHud(matrices, tickDelta));
	}
}
