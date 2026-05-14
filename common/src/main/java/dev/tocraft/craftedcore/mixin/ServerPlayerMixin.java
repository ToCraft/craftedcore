package dev.tocraft.craftedcore.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.tocraft.craftedcore.data.PlayerDataProvider;
import dev.tocraft.craftedcore.data.PlayerDataSynchronizer;
import dev.tocraft.craftedcore.event.common.PlayerEvents;
import dev.tocraft.craftedcore.registration.PlayerDataRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"DataFlowIssue", "unused", "RedundantCast"})
@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void serverTick(CallbackInfo info) {
        PlayerDataSynchronizer.sync((ServerPlayer) (Object) this);
    }

    @Inject(method = "restoreFrom", at = @At("RETURN"))
    private void restoreFrom(ServerPlayer serverPlayer, boolean bl, CallbackInfo ci) {
        PlayerEvents.PLAYER_RESPAWN.invoke().clone(serverPlayer, (ServerPlayer) (Object) this);
    }

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void copyPlayerData(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) {
        PlayerDataProvider oldDataProvider = (PlayerDataProvider) oldPlayer;
        PlayerDataProvider newDataProvider = (PlayerDataProvider) (Object) this;

        for (String key : oldDataProvider.craftedcore$keySet()) {
            if (PlayerDataRegistry.isKeyPersistent(key)) {
                // We're now dealing with the actual Java object, so no Class argument is needed here
                newDataProvider.craftedcore$writeTag(key, oldDataProvider.craftedcore$readTag(key));
            }
        }
    }

    @ModifyExpressionValue(method = "startSleepInBed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/attribute/BedRule;canSleep(Lnet/minecraft/world/level/Level;)Z"))
    private boolean fixSleep(boolean original) {
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