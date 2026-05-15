package dev.tocraft.craftedcore.neoforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.tocraft.craftedcore.event.common.PlayerEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings({"DataFlowIssue", "unused"})
@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @ModifyExpressionValue(
            method = "lambda$startSleepInBed$0",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/attribute/BedRule;canSleep(Lnet/minecraft/world/level/Level;)Z"
            )
    )
    private boolean fixStartSleep(boolean original) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        InteractionResult result = PlayerEvents.ALLOW_SLEEP_TIME.invoke().allowSleepTime(player, player.getSleepingPos().orElse(null), original);
        if (result == InteractionResult.FAIL) {
            return false;
        }
        if (result == InteractionResult.SUCCESS) {
            return true;
        }
        return original;
    }
}