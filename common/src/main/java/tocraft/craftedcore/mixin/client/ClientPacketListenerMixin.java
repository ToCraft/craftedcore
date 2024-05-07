package tocraft.craftedcore.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.event.client.ClientPlayerEvents;

@Environment(EnvType.CLIENT)
@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Unique
    private LocalPlayer craftedcore$oldPlayer = null;

    @Inject(method = "handleLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;setServerRenderDistance(I)V", shift = At.Shift.AFTER))
    private void handleLogin(ClientboundLoginPacket packet, CallbackInfo ci) {
        ClientPlayerEvents.CLIENT_PLAYER_JOIN.invoke().join(minecraft.player);
    }

    @Inject(method = "handleRespawn", at = @At("HEAD"))
    private void handleRespawnPre(ClientboundRespawnPacket packet, CallbackInfo ci) {
        this.craftedcore$oldPlayer = minecraft.player;
    }

    @Inject(method = "handleRespawn", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/ClientLevel;addPlayer(ILnet/minecraft/client/player/AbstractClientPlayer;)V"))
    private void handleRespawn(ClientboundRespawnPacket packet, CallbackInfo ci) {
        ClientPlayerEvents.CLIENT_PLAYER_RESPAWN.invoke().respawn(this.craftedcore$oldPlayer, minecraft.player);
        this.craftedcore$oldPlayer = null;
    }
}
