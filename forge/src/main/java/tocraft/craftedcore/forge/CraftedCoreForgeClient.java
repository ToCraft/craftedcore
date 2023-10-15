package tocraft.craftedcore.forge;

import net.minecraftforge.common.MinecraftForge;

public class CraftedCoreForgeClient {
	
	public CraftedCoreForgeClient() {
		MinecraftForge.EVENT_BUS.register(CraftedCoreForgeClient.class);
	}
}
