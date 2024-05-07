package tocraft.craftedcore.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.event.common.PlayerEvents;
import tocraft.craftedcore.registration.SynchronizedReloadListenerRegistry;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @ModifyArg(method = "reloadResources", at = @At(value = "INVOKE", target = "Lnet/minecraft/stats/ServerRecipeBook;sendInitialRecipeBook(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private ServerPlayer endResourceReload(ServerPlayer player) {
        SynchronizedReloadListenerRegistry.sendAllToPlayer(player);
        return player;
    }

    @Inject(method = "placeNewPlayer", at = @At("RETURN"))
    private void placeNewPlayer(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
        PlayerEvents.PLAYER_JOIN.invoke().join(serverPlayer);
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void remove(ServerPlayer serverPlayer, CallbackInfo ci) {
        PlayerEvents.PLAYER_QUIT.invoke().quit(serverPlayer);
    }
}
