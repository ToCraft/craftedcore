package tocraft.craftedcore.forge.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tocraft.craftedcore.event.client.ClientPlayerEvents;
import tocraft.craftedcore.event.client.ClientTickEvents;
import tocraft.craftedcore.event.client.RenderEvents;
import tocraft.craftedcore.registration.forge.KeyBindingRegistryImpl;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class CraftedCoreForgeEventHandlerClient {
    // FIXME: Where is RenderGuiEvent.Post ???
    @SubscribeEvent
    public void eventRenderGameOverlayEvent(CustomizeGuiOverlayEvent event) {
        RenderEvents.HUD_RENDERING.invoke().render(event.getPoseStack(), event.getPartialTick());
    }

    @SubscribeEvent
    public void event(RenderGuiOverlayEvent.Pre event) {
        switch (event.getOverlay().id().getPath()) {
            case "player_health" -> {
                InteractionResult result = RenderEvents.RENDER_HEALTH.invoke().render(event.getPoseStack(), Minecraft.getInstance().player);
                if (result == InteractionResult.FAIL) {
                    event.setCanceled(true);
                }
            }
            case "food_level" -> {
                InteractionResult result = RenderEvents.RENDER_FOOD.invoke().render(event.getPoseStack(), Minecraft.getInstance().player);
                if (result == InteractionResult.FAIL) {
                    event.setCanceled(true);
                }
            }
            case "air_level" -> {
                InteractionResult result = RenderEvents.RENDER_BREATH.invoke().render(event.getPoseStack(), Minecraft.getInstance().player);
                if (result == InteractionResult.FAIL) {
                    event.setCanceled(true);
                }
            }
            case "vehicle_health" -> {
                InteractionResult result = RenderEvents.RENDER_MOUNT_HEALTH.invoke().render(event.getPoseStack(), Minecraft.getInstance().player);
                if (result == InteractionResult.FAIL) {
                    event.setCanceled(true);
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
    public void registerKeyMappingsEvent(RegisterKeyMappingsEvent event) {
        for (KeyMapping mapping : KeyBindingRegistryImpl.getMappingsForEvent()) {
            event.register(mapping);
        }
    }

    @SubscribeEvent
    public void event(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientPlayerEvents.CLIENT_PLAYER_QUIT.invoke().quit(event.getPlayer());
    }
}
