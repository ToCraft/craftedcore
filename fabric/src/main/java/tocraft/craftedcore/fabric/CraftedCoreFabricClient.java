package tocraft.craftedcore.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import tocraft.craftedcore.client.CraftedCoreClient;
import tocraft.craftedcore.event.client.RenderEvents;

@Environment(EnvType.CLIENT)
public class CraftedCoreFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((graphics, tickDelta) -> RenderEvents.HUD_RENDERING.invoke().render(graphics, tickDelta));

        new CraftedCoreClient().initialize();
    }
}
