package dev.tocraft.craftedcore.fabric;

import dev.tocraft.craftedcore.CraftedCore;
import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings("unused")
@ApiStatus.Internal
public class CraftedCoreFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CraftedCoreFabricEventHandler.initialize();

        new CraftedCore().initialize();
    }
}
