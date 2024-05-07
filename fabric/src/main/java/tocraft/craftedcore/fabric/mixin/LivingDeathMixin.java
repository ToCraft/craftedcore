package tocraft.craftedcore.fabric.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.event.common.EntityEvents;

@Mixin(value = {LivingEntity.class, Player.class, ServerPlayer.class})
public class LivingDeathMixin {
    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void die(DamageSource source, CallbackInfo ci) {
        if (EntityEvents.LIVING_DEATH.invoke().die((LivingEntity) (Object) this, source) == InteractionResult.FAIL) {
            ci.cancel();
        }
    }
}
