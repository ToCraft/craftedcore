package tocraft.craftedcore.forge.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tocraft.craftedcore.event.common.EntityEvents;

// I have no idea, why this is so complicated for Forge...
@SuppressWarnings({"UnreachableCode", "DataFlowIssue"})
@Mixin(LivingEntity.class)
public abstract class LivingBreatheMixin extends Entity {
    public LivingBreatheMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

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

    @Inject(method = "baseTick", at = @At("HEAD"))
    private void breathe(CallbackInfo ci) {
        boolean isInWater = this.isEyeInFluid(FluidTags.WATER) && !this.level.getBlockState(new BlockPos(this.getX(), this.getEyeY(), this.getZ())).is(Blocks.BUBBLE_COLUMN);
        int air = this.getAirSupply();
        boolean canBreathe = EntityEvents.LIVING_BREATHE.invoke().breathe((LivingEntity) (Object) this, !isInWater);

        if (this.isAlive()) {
            if (!isInWater && !canBreathe) {
                this.setAirSupply(this.decreaseAirSupply(air));

                // Air has run out, start drowning
                if (this.getAirSupply() == -20) {
                    this.setAirSupply(0);
                    this.hurt(DamageSource.DRY_OUT, 2.0F);
                }
            } else if (canBreathe && isInWater) {
                this.setAirSupply(this.increaseAirSupply(air));
            }
        }
    }
}
