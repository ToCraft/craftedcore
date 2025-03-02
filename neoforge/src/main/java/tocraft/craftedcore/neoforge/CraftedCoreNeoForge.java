package tocraft.craftedcore.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.neoforge.client.CraftedCoreNeoForgeClient;

import java.util.Objects;

@SuppressWarnings("unused")
@Mod(CraftedCore.MODID)
public class CraftedCoreNeoForge {

    public CraftedCoreNeoForge(IEventBus bus) {
        if (FMLEnvironment.dist.isClient()) {
            new CraftedCoreNeoForgeClient();
        }
        new CraftedCore().initialize();

        NeoForge.EVENT_BUS.register(new CraftedCoreNeoForgeEventHandler());

    }

    public static IEventBus getEventBus() {
        return Objects.requireNonNull(ModList.get().getModContainerById(CraftedCore.MODID).orElseThrow().getEventBus());
    }
}
