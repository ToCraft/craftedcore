package tocraft.craftedcore.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import tocraft.craftedcore.client.CraftedCoreClient;

@Environment(EnvType.CLIENT)
public class CraftedCoreFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CraftedCoreFabricEventHandlerClient.initialize();

        new CraftedCoreClient().initialize();
    }
}
