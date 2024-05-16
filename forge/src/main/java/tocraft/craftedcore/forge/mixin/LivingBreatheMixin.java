package tocraft.craftedcore.forge.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tocraft.craftedcore.event.common.EntityEvents;

@SuppressWarnings({"DataFlowIssue", "unused"})
@Mixin(LivingEntity.class)
public class LivingBreatheMixin {
    @ModifyExpressionValue(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/FluidType;isAir()Z"))
    private boolean canBreathe(boolean canBreathe) {
        return EntityEvents.LIVING_BREATHE.invoke().breathe((LivingEntity) (Object) this, canBreathe);
    }

    @ModifyExpressionValue(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canDrownInFluidType(Lnet/minecraftforge/fluids/FluidType;)Z"))
    private boolean canDrown(boolean canDrown) {
        return !EntityEvents.LIVING_BREATHE.invoke().breathe((LivingEntity) (Object) this, !canDrown);
    }
}
