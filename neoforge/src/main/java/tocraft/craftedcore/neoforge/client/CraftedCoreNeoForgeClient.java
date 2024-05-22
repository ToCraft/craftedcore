package tocraft.craftedcore.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import tocraft.craftedcore.client.CraftedCoreClient;

@OnlyIn(Dist.CLIENT)
public class CraftedCoreNeoForgeClient {
    public CraftedCoreNeoForgeClient() {
        NeoForge.EVENT_BUS.register(new CraftedCoreNeoForgeEventHandlerClient());

        new CraftedCoreClient().initialize();
    }
}
