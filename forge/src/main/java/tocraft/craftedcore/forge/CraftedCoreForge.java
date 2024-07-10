package tocraft.craftedcore.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.forge.client.CraftedCoreForgeClient;
import tocraft.craftedcore.network.forge.ModernNetworkingImpl;

@SuppressWarnings("unused")
@Mod(CraftedCore.MODID)
public class CraftedCoreForge {

    public CraftedCoreForge() {
        ModernNetworkingImpl.initialize();

        if (FMLEnvironment.dist.isClient()) {
            new CraftedCoreForgeClient();
        }

        new CraftedCore().initialize();

        MinecraftForge.EVENT_BUS.register(new CraftedCoreForgeEventHandler());
    }
}
