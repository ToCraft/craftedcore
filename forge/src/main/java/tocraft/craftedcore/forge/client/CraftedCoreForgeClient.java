package tocraft.craftedcore.forge.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import tocraft.craftedcore.client.CraftedCoreClient;

@OnlyIn(Dist.CLIENT)
public class CraftedCoreForgeClient {

    public CraftedCoreForgeClient() {
        MinecraftForge.EVENT_BUS.register(new CraftedCoreForgeEventHandlerClient());

        new CraftedCoreClient().initialize();
    }
}
