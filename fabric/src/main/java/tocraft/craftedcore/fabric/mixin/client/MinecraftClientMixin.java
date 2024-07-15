package tocraft.craftedcore.fabric.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.event.client.ClientPlayerEvents;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {
    //#if MC>=1205
    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;Z)V",
                    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/GameNarrator;clear()V"))
    private void onDisconnect(Screen screen, boolean retainDownloadedPacks, CallbackInfo ci) {
    //#elseif MC>1201
    //$$     @Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;)V",
    //$$             at = @At(value = "INVOKE", target = "Lnet/minecraft/client/GameNarrator;clear()V"))
    //$$     private void onDisconnect(Screen nextScreen, CallbackInfo ci) {
    //#elseif MC>1182
    //$$         @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V",
    //$$                 at = @At(value = "INVOKE", target = "Lnet/minecraft/client/GameNarrator;clear()V"))
    //$$         private void onDisconnect(Screen nextScreen, CallbackInfo ci) {
    //#else
    //$$ @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V",
    //$$         at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/chat/NarratorChatListener;clear()V"))
    //$$ private void onDisconnect(Screen nextScreen, CallbackInfo ci) {
    //#endif
        ClientPlayerEvents.CLIENT_PLAYER_QUIT.invoke().quit(Minecraft.getInstance().player);
    }
}
