package dev.tocraft.craftedcore.fabric;

import dev.tocraft.craftedcore.event.common.CommandEvents;
import dev.tocraft.craftedcore.event.common.PlayerEvents;
import dev.tocraft.craftedcore.event.common.ServerLevelEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class CraftedCoreFabricEventHandler {

    public static void initialize() {
        EntitySleepEvents.ALLOW_SLEEPING.register((player, sleepingPos) -> {
            InteractionResult result = PlayerEvents.ALLOW_SLEEP_TIME.invoke().allowSleepTime(player, sleepingPos, true);
            return result == InteractionResult.FAIL ? Player.BedSleepingProblem.OTHER_PROBLEM : null;
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CommandEvents.REGISTRATION.invoke().register(dispatcher, registryAccess, environment));
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents.LOAD.register((server, world) -> ServerLevelEvents.LEVEL_LOAD.invoke().call(world));
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents.UNLOAD.register((server, world) -> ServerLevelEvents.LEVEL_UNLOAD.invoke().call(world));
    }
}
