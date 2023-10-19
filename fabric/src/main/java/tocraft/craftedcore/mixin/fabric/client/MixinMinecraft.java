package tocraft.craftedcore.mixin.fabric.client;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import tocraft.craftedcore.events.client.ClientPlayerEvents;

@Unique
@Mixin(Minecraft.class)
public class MixinMinecraft {
	@Shadow
    @Nullable
    public LocalPlayer player;
	
	@Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/GameNarrator;clear()V"))
    private void handleLogin(Screen screen, CallbackInfo ci) {
        ClientPlayerEvents.CLIENT_PLAYER_QUIT.invoker().quit(player);
    }
}
