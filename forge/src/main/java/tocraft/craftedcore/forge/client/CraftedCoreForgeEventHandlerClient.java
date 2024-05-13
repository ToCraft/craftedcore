package tocraft.craftedcore.forge.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tocraft.craftedcore.event.client.RenderEvents;
import tocraft.craftedcore.registration.forge.KeyBindingRegistryImpl;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class CraftedCoreForgeEventHandlerClient {
    // FIXME: Where is RenderGuiEvent.Post ???
    @SubscribeEvent
    public void eventRenderGameOverlayEvent(CustomizeGuiOverlayEvent event) {
        RenderEvents.HUD_RENDERING.invoke().render(event.getGuiGraphics(), event.getPartialTick());
    }

    @SubscribeEvent
    public void registerKeyMappingsEvent(RegisterKeyMappingsEvent event) {
        for (KeyMapping mapping : KeyBindingRegistryImpl.getMappingsForEvent()) {
            event.register(mapping);
        }
    }
}
