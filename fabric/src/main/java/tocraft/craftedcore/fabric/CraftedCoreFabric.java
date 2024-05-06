package tocraft.craftedcore.fabric;

import dev.architectury.registry.client.keymappings.fabric.KeyMappingRegistryImpl;
import net.fabricmc.api.ModInitializer;
import tocraft.craftedcore.CraftedCore;

public class CraftedCoreFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        new CraftedCore().initialize();
    }
}
