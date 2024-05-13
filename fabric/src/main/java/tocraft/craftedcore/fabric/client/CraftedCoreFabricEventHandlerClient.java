package tocraft.craftedcore.fabric.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import tocraft.craftedcore.event.client.RenderEvents;
import tocraft.craftedcore.event.common.CommandEvents;
import tocraft.craftedcore.event.common.PlayerEvents;

@Environment(EnvType.CLIENT)
public class CraftedCoreFabricEventHandlerClient {

    public static void initialize() {
        HudRenderCallback.EVENT.register((graphics, tickDelta) -> RenderEvents.HUD_RENDERING.invoke().render(graphics, tickDelta));
        ClientTickEvents.START_CLIENT_TICK.register(instance -> tocraft.craftedcore.event.client.ClientTickEvents.CLIENT_PRE.invoke().tick(instance));
        ClientTickEvents.END_CLIENT_TICK.register(instance -> tocraft.craftedcore.event.client.ClientTickEvents.CLIENT_POST.invoke().tick(instance));
    }
}
