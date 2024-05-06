package tocraft.craftedcore.neoforge.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import tocraft.craftedcore.event.client.RenderEvents;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class CraftedCoreNeoForgeEventHandlerClient {
    @SubscribeEvent
    public void event(RenderGuiEvent.Post event) {
        RenderEvents.HUD_RENDERING.invoke().render(event.getGuiGraphics(), event.getPartialTick());
    }

    @SubscribeEvent
    public void event(RenderGuiLayerEvent.Pre event) {
        switch (event.getName().getPath()) {
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
