package tocraft.craftedcore.fabric;

import net.fabricmc.api.ModInitializer;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.fabric.events.client.FabricEventHandlerClient;
import tocraft.craftedcore.fabric.registration.client.FabricRegistryHandlerClient;
import tocraft.craftedcore.platform.Platform;

public class CraftedCoreFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		new CraftedCore().initialize();
		
		if (Platform.getDist().isClient()) {
			new FabricEventHandlerClient();
			new FabricRegistryHandlerClient();
		}
	}
}
