package tocraft.craftedcore.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.forge.events.ForgeEvents;

@Mod(CraftedCore.MODID)
public class CraftedCoreForge {

	public CraftedCoreForge() {
		MinecraftForge.EVENT_BUS.register(ForgeEvents.class);
		
		getModVersion();
		new CraftedCore().initialize();
	}

	public void getModVersion() {
		ModContainer modContainer = ModList.get().getModContainerById(CraftedCore.MODID).get();
		CraftedCore.setVersion(modContainer.getModInfo().getVersion().toString());
	}
}
