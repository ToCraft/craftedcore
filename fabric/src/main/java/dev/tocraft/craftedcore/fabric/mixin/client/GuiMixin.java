package dev.tocraft.craftedcore.fabric.mixin.client;

import dev.tocraft.craftedcore.event.client.RenderEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@ApiStatus.Internal
@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    protected abstract Player getCameraPlayer();

    @Inject(method = "extractAirBubbles", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"), cancellable = true)
    private void shouldRenderBreath(GuiGraphicsExtractor guiGraphics, Player player, int i, int j, int k, CallbackInfo ci) {
        InteractionResult result = RenderEvents.RENDER_BREATH.invoke().render(guiGraphics, this.getCameraPlayer());
        if (result == InteractionResult.FAIL) {
            ci.cancel();
        }
    }

    @Inject(method = "extractHearts", at = @At(value = "HEAD"), cancellable = true)
    private void shouldRenderHealth(GuiGraphicsExtractor guiGraphics, Player player, int x, int y, int height, int offsetHeartIndex, float maxHealth, int currentHealth, int displayHealth, int absorptionAmount, boolean renderHighlight, CallbackInfo ci) {
        InteractionResult result = RenderEvents.RENDER_HEALTH.invoke().render(guiGraphics, player);
        if (result == InteractionResult.FAIL) {
            ci.cancel();
        }
    }

    @Inject(method = "extractFood", at = @At(value = "HEAD"), cancellable = true)
    private void shouldRenderFood(GuiGraphicsExtractor guiGraphics, Player player, int y, int x, CallbackInfo ci) {
        InteractionResult result = RenderEvents.RENDER_FOOD.invoke().render(guiGraphics, player);
        if (result == InteractionResult.FAIL) {
            ci.cancel();
        }
    }

    @Inject(method = "extractVehicleHealth", at = @At(value = "HEAD"), cancellable = true)
    private void shouldRenderMountHealth(GuiGraphicsExtractor guiGraphics, CallbackInfo ci) {
        InteractionResult result = RenderEvents.RENDER_MOUNT_HEALTH.invoke().render(guiGraphics, this.getCameraPlayer());
        if (result == InteractionResult.FAIL) {
            ci.cancel();
        }
    }
}
