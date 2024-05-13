package tocraft.craftedcore.forge.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tocraft.craftedcore.event.client.RenderEvents;

@OnlyIn(Dist.CLIENT)
@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    protected abstract Player getCameraPlayer();

    @Inject(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void shouldRenderBreath(GuiGraphics guiGraphics, CallbackInfo ci, Player player, int i, boolean bl, long l, int j, int k, int m, int n, float f, int o, int p, int q, int r, int s, LivingEntity livingEntity, int t, int u, int v) {
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
