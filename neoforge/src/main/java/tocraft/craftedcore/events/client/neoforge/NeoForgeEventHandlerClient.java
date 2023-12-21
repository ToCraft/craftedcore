package tocraft.craftedcore.events.client.neoforge;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.TickEvent;
import tocraft.craftedcore.events.client.ClientGuiEvents;
import tocraft.craftedcore.events.client.ClientPlayerEvents;
import tocraft.craftedcore.events.client.ClientTickEvents;

@OnlyIn(Dist.CLIENT)
public class NeoForgeEventHandlerClient {
	@SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ClientPlayerNetworkEvent.LoggingIn event) {
        ClientPlayerEvents.CLIENT_PLAYER_JOIN.invoker().join(event.getPlayer());
    }
	
	@SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            ClientTickEvents.CLIENT_PRE.invoker().tick(Minecraft.getInstance());
        else if (event.phase == TickEvent.Phase.END)
            ClientTickEvents.CLIENT_POST.invoker().tick(Minecraft.getInstance());
    }
	
	@SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventRenderGameOverlayEvent(RenderGuiEvent.Post event) {
        ClientGuiEvents.RENDER_HUD.invoker().renderHud(event.getGuiGraphics(), event.getPartialTick());
    }
	
	@SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientPlayerEvents.CLIENT_PLAYER_QUIT.invoker().quit(event.getPlayer());
    }
}
