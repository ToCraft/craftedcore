package tocraft.craftedcore.neoforge;

import net.neoforged.neoforge.common.NeoForge;
import tocraft.craftedcore.events.client.neoforge.NeoForgeEventHandlerClient;
import tocraft.craftedcore.network.neoforge.NetworkManagerImpl;

public class CraftedCoreNeoForgeClient {
	
	public CraftedCoreNeoForgeClient() {
		NeoForge.EVENT_BUS.register(NeoForgeEventHandlerClient.class);
		NeoForge.EVENT_BUS.register(NetworkManagerImpl.class);
	}
}
