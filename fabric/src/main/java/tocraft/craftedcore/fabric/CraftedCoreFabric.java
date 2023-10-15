package tocraft.craftedcore.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import tocraft.craftedcore.CraftedCore;

public class CraftedCoreFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		getModVersion();
		new CraftedCore().initialize();
	}

	public void getModVersion() {
		ModContainer modContainer = FabricLoader.getInstance().getModContainer(CraftedCore.MODID).get();
		CraftedCore.setVersion(modContainer.getMetadata().getVersion().getFriendlyString());
	}
}
