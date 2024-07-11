package tocraft.craftedcore.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
//#if MC>1201
//$$ import net.minecraft.server.network.CommonListenerCookie;
//#endif
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.event.common.PlayerEvents;
import tocraft.craftedcore.event.common.ResourceEvents;

@SuppressWarnings("unused")
@Mixin(PlayerList.class)
public class PlayerListMixin {
    @ModifyArg(method = "reloadResources", at = @At(value = "INVOKE", target = "Lnet/minecraft/stats/ServerRecipeBook;sendInitialRecipeBook(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private ServerPlayer endResourceReload(ServerPlayer player) {
        ResourceEvents.DATA_PACK_SYNC.invoke().onSync(player);
        return player;
    }


    @Inject(method = "placeNewPlayer", at = @At("RETURN"))
    //#if MC>1201
    //$$ private void placeNewPlayer(Connection connection, ServerPlayer serverPlayer, CommonListenerCookie commonListenerCookie, CallbackInfo ci) {
    //#else
    private void placeNewPlayer(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
    //#endif
        PlayerEvents.PLAYER_JOIN.invoke().join(serverPlayer);
        ResourceEvents.DATA_PACK_SYNC.invoke().onSync(serverPlayer);
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void remove(ServerPlayer serverPlayer, CallbackInfo ci) {
        PlayerEvents.PLAYER_QUIT.invoke().quit(serverPlayer);
    }
}
