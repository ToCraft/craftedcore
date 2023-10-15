package tocraft.craftedcore.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.forge.events.common.ForgeEventHandler;
import tocraft.craftedcore.platform.Platform;

@Mod(CraftedCore.MODID)
public class CraftedCoreForge {

	public CraftedCoreForge() {
		MinecraftForge.EVENT_BUS.register(ForgeEventHandler.class);
		
		if (Platform.getDist().isClient())
			new CraftedCoreForgeClient();
		
		new CraftedCore().initialize();
	}
}
