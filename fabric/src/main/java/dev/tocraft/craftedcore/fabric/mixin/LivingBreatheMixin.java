package dev.tocraft.craftedcore.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import dev.tocraft.craftedcore.event.common.EntityEvents;

@SuppressWarnings({"DataFlowIssue", "unused"})
@ApiStatus.Internal
@Mixin(LivingEntity.class)
public class LivingBreatheMixin {
    @ModifyExpressionValue(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean canBreathe(boolean cantBreathe) {
        return !EntityEvents.LIVING_BREATHE.invoke().breathe((LivingEntity) (Object) this, !cantBreathe);
    }
}
