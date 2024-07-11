package tocraft.craftedcore.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.neoforge.client.CraftedCoreNeoForgeClient;
//#if MC>=1202 && MC<1205
//$$ import tocraft.craftedcore.network.neoforge.ModernNetworkingImpl;
//#elseif MC>1205
import java.util.Objects;
//#endif

@SuppressWarnings("unused")
@Mod(CraftedCore.MODID)
public class CraftedCoreNeoForge {

    public CraftedCoreNeoForge(IEventBus bus) {
        if (FMLEnvironment.dist.isClient()) {
            new CraftedCoreNeoForgeClient();
        }
        new CraftedCore().initialize();

        NeoForge.EVENT_BUS.register(new CraftedCoreNeoForgeEventHandler());

        //#if MC==1204
        //$$ ModernNetworkingImpl.initialize(bus);
        //#else
        ModernNetworkingImpl.initialize();
        //#endif
    }

    //#if MC>=1206
    public static IEventBus getEventBus() {
        return Objects.requireNonNull(ModList.get().getModContainerById(CraftedCore.MODID).orElseThrow().getEventBus());
    }
    //#endif
}
