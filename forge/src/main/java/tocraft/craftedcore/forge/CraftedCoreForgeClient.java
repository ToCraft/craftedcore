package tocraft.craftedcore.forge;

import net.minecraftforge.common.MinecraftForge;
import tocraft.craftedcore.events.client.forge.ForgeEventHandlerClient;
import tocraft.craftedcore.network.forge.NetworkManagerImpl;

public class CraftedCoreForgeClient {
	
	public CraftedCoreForgeClient() {
		MinecraftForge.EVENT_BUS.register(ForgeEventHandlerClient.class);
		MinecraftForge.EVENT_BUS.register(NetworkManagerImpl.class);
	}
}
