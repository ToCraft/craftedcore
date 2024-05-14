package tocraft.craftedcore.neoforge.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiOverlayEvent;
import net.neoforged.neoforge.event.TickEvent;
import tocraft.craftedcore.event.client.ClientTickEvents;
import tocraft.craftedcore.event.client.RenderEvents;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class CraftedCoreNeoForgeEventHandlerClient {
    @SubscribeEvent
    public void event(RenderGuiEvent.Post event) {
        RenderEvents.HUD_RENDERING.invoke().render(event.getGuiGraphics(), event.getPartialTick());
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
    public void event(RenderGuiOverlayEvent.Pre event) {
        switch (event.getOverlay().id().getPath()) {
            case "player_health" -> {
                InteractionResult result = RenderEvents.RENDER_HEALTH.invoke().render(event.getGuiGraphics(), Minecraft.getInstance().player);
                if (result == InteractionResult.FAIL) {
                    event.setCanceled(true);
                }
            }
            case "food_level" -> {
                InteractionResult result = RenderEvents.RENDER_FOOD.invoke().render(event.getGuiGraphics(), Minecraft.getInstance().player);
                if (result == InteractionResult.FAIL) {
                    event.setCanceled(true);
                }
            }
            case "air_level" -> {
                InteractionResult result = RenderEvents.RENDER_BREATH.invoke().render(event.getGuiGraphics(), Minecraft.getInstance().player);
                if (result == InteractionResult.FAIL) {
                    event.setCanceled(true);
                }
            }
            case "vehicle_health" -> {
                InteractionResult result = RenderEvents.RENDER_MOUNT_HEALTH.invoke().render(event.getGuiGraphics(), Minecraft.getInstance().player);
                if (result == InteractionResult.FAIL) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
