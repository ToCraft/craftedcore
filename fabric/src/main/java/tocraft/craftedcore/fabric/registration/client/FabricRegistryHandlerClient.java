package tocraft.craftedcore.fabric.registration.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import tocraft.craftedcore.registration.client.KeyMappingRegistry;

public class FabricRegistryHandlerClient {
	
    public FabricRegistryHandlerClient() {
    	KeyMappingRegistry.MAPPINGS.forEach(KeyBindingHelper::registerKeyBinding);
    }
}
