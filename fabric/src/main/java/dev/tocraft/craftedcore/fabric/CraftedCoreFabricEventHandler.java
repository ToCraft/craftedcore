package dev.tocraft.craftedcore.fabric;

import dev.tocraft.craftedcore.event.common.CommandEvents;
import dev.tocraft.craftedcore.event.common.PlayerEvents;
import dev.tocraft.craftedcore.event.common.ServerLevelEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.util.EventResult;
import net.minecraft.world.InteractionResult;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class CraftedCoreFabricEventHandler {

    public static void initialize() {
        EntitySleepEvents.ALLOW_NEARBY_MONSTERS.register(((player, sleepingPos, vanillaResult) -> {
            InteractionResult result = PlayerEvents.ALLOW_MONSTERS_NEARBY.invoke().allowMonstersNearby(player, sleepingPos, vanillaResult);
            if (result == InteractionResult.SUCCESS) {
                return EventResult.ALLOW;
            } else if (result == InteractionResult.FAIL) {
                return EventResult.DENY;
            }else {
                return EventResult.PASS;
            }
        }));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CommandEvents.REGISTRATION.invoke().register(dispatcher, registryAccess, environment));
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents.LOAD.register((server, world) -> ServerLevelEvents.LEVEL_LOAD.invoke().call(world));
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents.UNLOAD.register((server, world) -> ServerLevelEvents.LEVEL_UNLOAD.invoke().call(world));
    }
    public static String getDayType(String day) {
        // The switch expression directly returns the result
        return switch (day) {
            case "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" -> "Weekday";
            case "Saturday", "Sunday" -> "Weekend";
            default -> "Invalid day";
        }; // Note the required semicolon here
    }
}
