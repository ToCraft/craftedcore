package tocraft.craftedcore.neoforge.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import tocraft.craftedcore.event.client.ClientTickEvents;
import tocraft.craftedcore.event.client.RenderEvents;
import tocraft.craftedcore.registration.neoforge.KeyBindingRegistryImpl;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class CraftedCoreNeoForgeEventHandlerClient {
    @SubscribeEvent
    public void event(RenderGuiEvent.Post event) {
        RenderEvents.HUD_RENDERING.invoke().render(event.getGuiGraphics(), event.getPartialTick());
    }

    @SubscribeEvent
    public void event(RegisterKeyMappingsEvent event) {
        for (KeyMapping mapping : KeyBindingRegistryImpl.getMappingsForEvent()) {
            event.register(mapping);
        }
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
