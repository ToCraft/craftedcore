package dev.tocraft.craftedcore.fabric;

import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.ApiStatus;
import dev.tocraft.craftedcore.CraftedCore;

@SuppressWarnings("unused")
@ApiStatus.Internal
public class CraftedCoreFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CraftedCoreFabricEventHandler.initialize();

        new CraftedCore().initialize();
    }
}
