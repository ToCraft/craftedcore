package tocraft.craftedcore.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.data.PlayerDataProvider;
import tocraft.craftedcore.data.PlayerDataSynchronizer;
import tocraft.craftedcore.registration.PlayerDataRegistry;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void serverTick(CallbackInfo info) {
        PlayerDataSynchronizer.sync((ServerPlayer) (Object) this);
    }

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void copyPlayerData(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) {
        PlayerDataProvider oldDataProvider = (PlayerDataProvider) oldPlayer;
        PlayerDataProvider newDataProvider = (PlayerDataProvider) (Object) this;

        for (String key : oldDataProvider.craftedcore$keySet()) {
            // is the entry persistent (will it be saved after death) ?
            if (PlayerDataRegistry.isKeyPersistent(key)) {
                newDataProvider.craftedcore$writeTag(key, oldDataProvider.craftedcore$readTag(key));
            }
        }
    }
}
