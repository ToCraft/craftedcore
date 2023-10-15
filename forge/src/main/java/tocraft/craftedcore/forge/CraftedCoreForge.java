package tocraft.craftedcore.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.forge.events.ForgeEvents;

@Mod(CraftedCore.MODID)
public class CraftedCoreForge {

	public CraftedCoreForge() {
		MinecraftForge.EVENT_BUS.register(ForgeEvents.class);
		
		new CraftedCore().initialize();
	}
}
