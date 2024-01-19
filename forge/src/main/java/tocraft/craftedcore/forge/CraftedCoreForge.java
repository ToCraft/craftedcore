package tocraft.craftedcore.forge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.client.CraftedCoreClient;

@Mod(CraftedCore.MODID)
public class CraftedCoreForge {

    public CraftedCoreForge() {
        if (FMLEnvironment.dist.isClient())
            new CraftedCoreClient().initialize();

        new CraftedCore().initialize();
    }
}
