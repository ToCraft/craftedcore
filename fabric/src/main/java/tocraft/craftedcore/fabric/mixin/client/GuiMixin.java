package tocraft.craftedcore.fabric.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.event.client.RenderEvents;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    protected abstract Player getCameraPlayer();

    @Inject(method = "renderAirBubbles", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"), cancellable = true)
    private void shouldRenderBreath(GuiGraphics guiGraphics, Player player, int i, int j, int k, CallbackInfo ci) {
        InteractionResult result = RenderEvents.RENDER_BREATH.invoke().render(guiGraphics, this.getCameraPlayer());
        if (result == InteractionResult.FAIL) {
            ci.cancel();
        }
    }

    @Inject(method = "renderHearts", at = @At(value = "HEAD"), cancellable = true)
    private void shouldRenderHealth(GuiGraphics guiGraphics, Player player, int x, int y, int height, int offsetHeartIndex, float maxHealth, int currentHealth, int displayHealth, int absorptionAmount, boolean renderHighlight, CallbackInfo ci) {
        InteractionResult result = RenderEvents.RENDER_HEALTH.invoke().render(guiGraphics, player);
        if (result == InteractionResult.FAIL) {
            ci.cancel();
        }
    }

    @Inject(method = "renderFood", at = @At(value = "HEAD"), cancellable = true)
    private void shouldRenderFood(GuiGraphics guiGraphics, Player player, int y, int x, CallbackInfo ci) {
        InteractionResult result = RenderEvents.RENDER_FOOD.invoke().render(guiGraphics, player);
        if (result == InteractionResult.FAIL) {
            ci.cancel();
        }
    }

    @Inject(method = "renderVehicleHealth", at = @At(value = "HEAD"), cancellable = true)
    private void shouldRenderMountHealth(GuiGraphics guiGraphics, CallbackInfo ci) {
        InteractionResult result = RenderEvents.RENDER_MOUNT_HEALTH.invoke().render(guiGraphics, this.getCameraPlayer());
        if (result == InteractionResult.FAIL) {
            ci.cancel();
        }
    }
}
