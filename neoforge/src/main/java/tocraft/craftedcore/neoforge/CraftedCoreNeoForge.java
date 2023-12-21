package tocraft.craftedcore.neoforge;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.events.common.neoforge.NeoForgeEventHandler;
import tocraft.craftedcore.network.neoforge.NetworkManagerImpl;
import tocraft.craftedcore.platform.Platform;

@Mod(CraftedCore.MODID)
public class CraftedCoreNeoForge {

	public CraftedCoreNeoForge() {
		NeoForge.EVENT_BUS.register(NeoForgeEventHandler.class);
		NeoForge.EVENT_BUS.register(NetworkManagerImpl.class);
		
		if (Platform.getDist().isClient())
			new CraftedCoreNeoForgeClient();
		
		new CraftedCore().initialize();
	}
}
