package tocraft.craftedcore.forge;

import net.minecraftforge.common.MinecraftForge;
import tocraft.craftedcore.forge.events.client.ForgeEventHandlerClient;

public class CraftedCoreForgeClient {
	
	public CraftedCoreForgeClient() {
		MinecraftForge.EVENT_BUS.register(ForgeEventHandlerClient.class);
	}
}
