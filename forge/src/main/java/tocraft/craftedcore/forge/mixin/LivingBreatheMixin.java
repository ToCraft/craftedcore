package tocraft.craftedcore.forge.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tocraft.craftedcore.event.common.EntityEvents;

// I have no idea, why this is so complicated for Forge...
@SuppressWarnings({"DataFlowIssue", "unused", "UnreachableCode"})
@Mixin(LivingEntity.class)
public abstract class LivingBreatheMixin {
    @Shadow
    public abstract boolean isAlive();

    @Shadow
    protected abstract int increaseAirSupply(int currentAir);

    @Shadow
    protected abstract int decreaseAirSupply(int currentAir);

    @Inject(method = "increaseAirSupply", at = @At("RETURN"), cancellable = true)
    private void onIncreaseAirSupply(int currentAir, CallbackInfoReturnable<Integer> cir) {
        if (!EntityEvents.LIVING_BREATHE.invoke().breathe((LivingEntity) (Object) this, true)) {
            cir.setReturnValue(currentAir);
        }
    }

    @Inject(method = "decreaseAirSupply", at = @At("RETURN"), cancellable = true)
    private void onDecreaseAirSupply(int currentAir, CallbackInfoReturnable<Integer> cir) {
        if (EntityEvents.LIVING_BREATHE.invoke().breathe((LivingEntity) (Object) this, false)) {
            cir.setReturnValue(currentAir);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void breathe(CallbackInfo ci) {
        if (this.isAlive()) {
            int air = ((LivingEntity) (Object) this).getAirSupply();
            if (!EntityEvents.LIVING_BREATHE.invoke().breathe((LivingEntity) (Object) this, !((LivingEntity) (Object) this).isInWater())) {
                ((LivingEntity) (Object) this).setAirSupply(this.decreaseAirSupply(air));

                // Air has run out, start drowning
                if (((LivingEntity) (Object) this).getAirSupply() == -20) {
                    ((LivingEntity) (Object) this).setAirSupply(0);
                    ((LivingEntity) (Object) this).hurt(DamageSource.DRY_OUT, 2.0F);
                }
            } else if (((LivingEntity) (Object) this).getAirSupply() < ((LivingEntity) (Object) this).getMaxAirSupply()) {
                ((LivingEntity) (Object) this).setAirSupply(this.increaseAirSupply(air));
            }
        }
    }
}
