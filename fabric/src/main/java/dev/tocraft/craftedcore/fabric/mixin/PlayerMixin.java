package dev.tocraft.craftedcore.fabric.mixin;

import dev.tocraft.craftedcore.event.common.PlayerEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@ApiStatus.Internal
@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    private void destroySpeed(BlockState state, @NotNull CallbackInfoReturnable<Float> cir) {
        float newSpeed = PlayerEvents.DESTROY_SPEED.invoke().setDestroySpeed((Player) (Object) this, cir.getReturnValue());
        cir.setReturnValue(newSpeed);
    }
}
