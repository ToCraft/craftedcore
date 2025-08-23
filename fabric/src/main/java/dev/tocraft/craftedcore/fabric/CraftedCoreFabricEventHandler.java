package dev.tocraft.craftedcore.fabric;

import dev.tocraft.craftedcore.event.common.CommandEvents;
import dev.tocraft.craftedcore.event.common.PlayerEvents;
import dev.tocraft.craftedcore.event.common.ServerLevelEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class CraftedCoreFabricEventHandler {

    public static void initialize() {
        EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, sleepingPos, vanillaResult) -> PlayerEvents.ALLOW_SLEEP_TIME.invoke().allowSleepTime(player, sleepingPos, vanillaResult));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CommandEvents.REGISTRATION.invoke().register(dispatcher, registryAccess, environment));
        ServerWorldEvents.LOAD.register((server, world) -> ServerLevelEvents.LEVEL_LOAD.invoke().call(world));
        ServerWorldEvents.UNLOAD.register((server, world) -> ServerLevelEvents.LEVEL_UNLOAD.invoke().call(world));
    }
}
