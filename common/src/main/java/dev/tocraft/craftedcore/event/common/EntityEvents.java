package dev.tocraft.craftedcore.event.common;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import dev.tocraft.craftedcore.event.Event;
import dev.tocraft.craftedcore.event.EventFactory;

@SuppressWarnings({"unused", "SameReturnValue"})
public final class EntityEvents {
    public static final Event<Interact> INTERACT_WITH_PLAYER = EventFactory.createWithInteractionResult();
    public static final Event<LivingDeath> LIVING_DEATH = EventFactory.createWithInteractionResult();
    public static final Event<LivingBreathe> LIVING_BREATHE = EventFactory.createWithCallback(callbacks -> (entity, canBreathe) -> {
        boolean canBreatheNow = canBreathe;
        for (LivingBreathe callback : callbacks) {
            canBreatheNow = callback.breathe(entity, canBreatheNow);
        }
        return canBreatheNow;
    });

    @FunctionalInterface
    public interface Interact {
        InteractionResult interact(Player player, Entity entity, InteractionHand hand);
    }

    @FunctionalInterface
    public interface LivingDeath {
        InteractionResult die(LivingEntity entity, DamageSource source);
    }

    @FunctionalInterface
    public interface LivingBreathe {
        /**
         * @return the new value for canBreathe
         */
        boolean breathe(LivingEntity entity, boolean canBreathe);
    }
}
