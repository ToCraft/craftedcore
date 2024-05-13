package tocraft.craftedcore.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.neoforge.client.CraftedCoreNeoForgeClient;

@SuppressWarnings("unused")
@Mod(CraftedCore.MODID)
public class CraftedCoreNeoForge {

    public CraftedCoreNeoForge(IEventBus bus) {
        if (FMLEnvironment.dist.isClient()) {
            new CraftedCoreNeoForgeClient(bus);
        }
        new CraftedCore().initialize();

        NeoForge.EVENT_BUS.register(new CraftedCoreNeoForgeEventHandler());
    }
}
