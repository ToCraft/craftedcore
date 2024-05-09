package tocraft.craftedcore.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.InteractionResult;
import org.jetbrains.annotations.ApiStatus;
import tocraft.craftedcore.event.client.ClientTickEvents;
import tocraft.craftedcore.event.common.CommandEvents;
import tocraft.craftedcore.event.common.EntityEvents;
import tocraft.craftedcore.event.common.ServerLevelEvents;

public final class ArchitecturyImpl {
    public static void initialize() {
        LifecycleEvent.SERVER_LEVEL_LOAD.register(world -> ServerLevelEvents.LEVEL_LOAD.invoke().call(world));
        LifecycleEvent.SERVER_LEVEL_UNLOAD.register(world -> ServerLevelEvents.LEVEL_UNLOAD.invoke().call(world));
        InteractionEvent.INTERACT_ENTITY.register(((player, entity, hand) -> convertInteractionToEventResult(EntityEvents.INTERACT_WITH_PLAYER.invoke().interact(player, entity, hand))));
        CommandRegistrationEvent.EVENT.register((dispatcher, selection) -> CommandEvents.REGISTRATION.invoke().register(dispatcher, selection));
    }

    @ApiStatus.Internal
    private static EventResult convertInteractionToEventResult(InteractionResult result) {
        if (result == InteractionResult.FAIL) {
            return EventResult.interruptFalse();
        } else if (result == InteractionResult.SUCCESS) {
            return EventResult.interruptTrue();
        } else {
            return EventResult.pass();
        }
    }

    @Environment(EnvType.CLIENT)
    public static void clientInitialize() {
        ClientTickEvent.CLIENT_PRE.register(instance -> ClientTickEvents.CLIENT_PRE.invoke().tick(instance));
        ClientTickEvent.CLIENT_POST.register(instance -> ClientTickEvents.CLIENT_POST.invoke().tick(instance));
        ClientTickEvent.CLIENT_LEVEL_PRE.register(instance -> ClientTickEvents.CLIENT_LEVEL_PRE.invoke().tick(instance));
        ClientTickEvent.CLIENT_LEVEL_POST.register(instance -> ClientTickEvents.CLIENT_LEVEL_POST.invoke().tick(instance));
    }
}
