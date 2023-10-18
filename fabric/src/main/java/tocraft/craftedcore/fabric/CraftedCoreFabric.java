package tocraft.craftedcore.fabric;

import net.fabricmc.api.ModInitializer;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.events.client.fabric.FabricEventHandlerClient;
import tocraft.craftedcore.events.common.fabric.FabricEventHandler;
import tocraft.craftedcore.platform.Platform;

public class CraftedCoreFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		new CraftedCore().initialize();
		new FabricEventHandler();
		
		if (Platform.getDist().isClient()) {
			new FabricEventHandlerClient();
		}
	}
}
