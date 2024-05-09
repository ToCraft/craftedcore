package tocraft.craftedcore.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.event.common.PlayerEvents;

public class CraftedCoreFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        new CraftedCore().initialize();

        EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, sleepingPos, vanillaResult) -> PlayerEvents.ALLOW_SLEEP_TIME.invoke().allowSleepTime(player, sleepingPos, vanillaResult));
    }
}
