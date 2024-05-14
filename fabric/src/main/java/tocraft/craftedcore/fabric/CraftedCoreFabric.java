package tocraft.craftedcore.fabric;

import net.fabricmc.api.ModInitializer;
import tocraft.craftedcore.CraftedCore;

@SuppressWarnings("unused")
public class CraftedCoreFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CraftedCoreFabricEventHandler.initialize();

        new CraftedCore().initialize();
    }
}
