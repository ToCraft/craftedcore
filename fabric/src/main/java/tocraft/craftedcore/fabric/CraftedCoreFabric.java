package tocraft.craftedcore.fabric;

import net.fabricmc.api.ModInitializer;
import tocraft.craftedcore.CraftedCore;

public class CraftedCoreFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        new CraftedCore().initialize();
    }
}
