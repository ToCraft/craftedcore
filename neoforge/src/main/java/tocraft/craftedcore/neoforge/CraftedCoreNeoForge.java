package tocraft.craftedcore.neoforge;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import tocraft.craftedcore.CraftedCore;

@Mod(CraftedCore.MODID)
public class CraftedCoreNeoForge {

    public CraftedCoreNeoForge() {
        if (FMLEnvironment.dist.isClient())
            new CraftedCoreNeoForgeClient();
        new CraftedCore().initialize();
    }
}
