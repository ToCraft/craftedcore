package tocraft.craftedcore.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.event.client.ClientPlayerEvents;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {
    @Shadow
    @Nullable
    public LocalPlayer player;

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/GameNarrator;clear()V"))
    private void onDisconnect(Screen screen, boolean retainDownloadedPacks, CallbackInfo ci) {
        ClientPlayerEvents.CLIENT_PLAYER_QUIT.invoke().quit(this.player);
    }
}
