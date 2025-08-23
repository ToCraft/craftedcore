package dev.tocraft.craftedcore.neoforge.client;

import dev.tocraft.craftedcore.event.client.ClientPlayerEvents;
import dev.tocraft.craftedcore.event.client.ClientTickEvents;
import dev.tocraft.craftedcore.event.client.RenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@ApiStatus.Internal
@OnlyIn(Dist.CLIENT)
public class CraftedCoreNeoForgeEventHandlerClient {
    @SubscribeEvent
    public void event(RenderGuiEvent.@NotNull Post event) {
        RenderEvents.HUD_RENDERING.invoke().render(event.getGuiGraphics(), event.getPartialTick());
    }

    @SubscribeEvent
    public void event(ClientTickEvent.Pre event) {
        ClientTickEvents.CLIENT_PRE.invoke().tick(Minecraft.getInstance());
    }

    @SubscribeEvent
    public void event(ClientTickEvent.Post event) {
        ClientTickEvents.CLIENT_POST.invoke().tick(Minecraft.getInstance());
    }

    @SubscribeEvent
    public void event(RenderGuiLayerEvent.@NotNull Pre event) {
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

    @SubscribeEvent
    public void event(ClientPlayerNetworkEvent.@NotNull LoggingOut event) {
        ClientPlayerEvents.CLIENT_PLAYER_QUIT.invoke().quit(event.getPlayer());
    }
}
