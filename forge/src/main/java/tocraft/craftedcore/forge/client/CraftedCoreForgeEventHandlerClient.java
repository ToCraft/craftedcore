package tocraft.craftedcore.forge.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tocraft.craftedcore.event.client.ClientPlayerEvents;
import tocraft.craftedcore.event.client.ClientTickEvents;
import tocraft.craftedcore.event.client.RenderEvents;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class CraftedCoreForgeEventHandlerClient {
    // FIXME: Where is RenderGuiEvent.Post ???
    @SubscribeEvent
    public void eventRenderGameOverlayEvent(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            RenderEvents.HUD_RENDERING.invoke().render(event.getMatrixStack(), event.getPartialTicks());
        }
    }

    @SubscribeEvent
    public void event(RenderGameOverlayEvent.PreLayer event) {
        OverlayRegistry.OverlayEntry overlayEntry = OverlayRegistry.getEntry(event.getOverlay());
        if (overlayEntry != null) {
            switch (overlayEntry.getDisplayName()) {
                case "Player Health" -> {
                    InteractionResult result = RenderEvents.RENDER_HEALTH.invoke().render(event.getMatrixStack(), Minecraft.getInstance().player);
                    if (result == InteractionResult.FAIL) {
                        event.setCanceled(true);
                    }
                }
                case "Food Level" -> {
                    InteractionResult result = RenderEvents.RENDER_FOOD.invoke().render(event.getMatrixStack(), Minecraft.getInstance().player);
                    if (result == InteractionResult.FAIL) {
                        event.setCanceled(true);
                    }
                }
                case "Air Level" -> {
                    InteractionResult result = RenderEvents.RENDER_BREATH.invoke().render(event.getMatrixStack(), Minecraft.getInstance().player);
                    if (result == InteractionResult.FAIL) {
                        event.setCanceled(true);
                    }
                }
                case "Mount Health" -> {
                    InteractionResult result = RenderEvents.RENDER_MOUNT_HEALTH.invoke().render(event.getMatrixStack(), Minecraft.getInstance().player);
                    if (result == InteractionResult.FAIL) {
                        event.setCanceled(true);
                    }
                }
            }
        }
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
    public void event(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        ClientPlayerEvents.CLIENT_PLAYER_QUIT.invoke().quit(event.getPlayer());
    }
}
