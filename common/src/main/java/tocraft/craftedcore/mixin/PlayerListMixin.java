package tocraft.craftedcore.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tocraft.craftedcore.registration.SynchronizedReloadListenerRegistry;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @ModifyArg(method = "reloadResources", at = @At(value = "INVOKE", target = "Lnet/minecraft/stats/ServerRecipeBook;sendInitialRecipeBook(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private ServerPlayer endResourceReload(ServerPlayer player) {
        SynchronizedReloadListenerRegistry.sendAllToPlayer(player);
        return player;
    }
}
