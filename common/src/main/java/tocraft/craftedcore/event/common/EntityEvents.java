package tocraft.craftedcore.event.common;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import tocraft.craftedcore.event.Event;
import tocraft.craftedcore.event.EventFactory;

@SuppressWarnings("unused")
public final class EntityEvents {
    public static final Event<InteractionEvent> INTERACT_WITH_PLAYER = EventFactory.createWithInteractionResult();

    @FunctionalInterface
    public interface InteractionEvent {
        InteractionResult interact(Player player, Entity entity, InteractionHand hand);
    }
}
