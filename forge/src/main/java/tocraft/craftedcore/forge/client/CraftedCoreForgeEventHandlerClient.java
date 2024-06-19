package tocraft.craftedcore.forge.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tocraft.craftedcore.event.client.ClientPlayerEvents;
import tocraft.craftedcore.event.client.ClientTickEvents;
import tocraft.craftedcore.event.client.RenderEvents;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class CraftedCoreForgeEventHandlerClient {
    @SubscribeEvent
    public void event(CustomizeGuiOverlayEvent event) {
        RenderEvents.HUD_RENDERING.invoke().render(event.getGuiGraphics(), DeltaTracker.ONE);
    }

    @SubscribeEvent
    public void event(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ClientTickEvents.CLIENT_PRE.invoke().tick(Minecraft.getInstance());
        } else if (event.phase == TickEvent.Phase.END) {
            ClientTickEvents.CLIENT_POST.invoke().tick(Minecraft.getInstance());
        }
    }

    @SubscribeEvent
    public void event(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientPlayerEvents.CLIENT_PLAYER_QUIT.invoke().quit(event.getPlayer());
    }
}
