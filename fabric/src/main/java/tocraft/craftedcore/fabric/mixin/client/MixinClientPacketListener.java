package tocraft.craftedcore.fabric.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import tocraft.craftedcore.events.client.ClientPlayerEvents;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {
    @Shadow
    @Final
    private Minecraft minecraft;
    
    @Inject(method = "handleLogin", at = @At("RETURN"))
    private void handleLogin(ClientboundLoginPacket packet, CallbackInfo ci) {
        ClientPlayerEvents.CLIENT_PLAYER_JOIN.invoker().join(minecraft.player);
    }
}
