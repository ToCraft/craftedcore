package dev.tocraft.craftedcore.fabric.client;

import dev.tocraft.craftedcore.event.client.RenderEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public class CraftedCoreFabricEventHandlerClient {

    public static void initialize() {
        HudRenderCallback.EVENT.register((graphics, tickCounter) -> RenderEvents.HUD_RENDERING.invoke().render(graphics, tickCounter));
        ClientTickEvents.START_CLIENT_TICK.register(instance -> dev.tocraft.craftedcore.event.client.ClientTickEvents.CLIENT_PRE.invoke().tick(instance));
        ClientTickEvents.END_CLIENT_TICK.register(instance -> dev.tocraft.craftedcore.event.client.ClientTickEvents.CLIENT_POST.invoke().tick(instance));
    }
}
