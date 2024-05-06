package tocraft.craftedcore.forge.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.event.client.RenderEvents;

@OnlyIn(Dist.CLIENT)
@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    protected abstract Player getCameraPlayer();

    @ModifyExpressionValue(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean shouldRenderBreath(boolean isEyeInFluid) {
        InteractionResult result = RenderEvents.RENDER_BREATH.invoke().render(this.getCameraPlayer());
        if (result == InteractionResult.FAIL) {
            return false;
        } else {
            return isEyeInFluid;
        }
    }

    @Inject(method = "renderHearts", at = @At(value = "HEAD"), cancellable = true)
    private void shouldRenderHealth(GuiGraphics guiGraphics, Player player, int x, int y, int height, int offsetHeartIndex, float maxHealth, int currentHealth, int displayHealth, int absorptionAmount, boolean renderHighlight, CallbackInfo ci) {
        InteractionResult result = RenderEvents.RENDER_HEALTH.invoke().render(player, guiGraphics);
        if (result == InteractionResult.FAIL) {
            ci.cancel();
        }
    }

    @Inject(method = "renderFood", at = @At(value = "HEAD"), cancellable = true)
    private void shouldRenderFood(GuiGraphics guiGraphics, Player player, int y, int x, CallbackInfo ci) {
        InteractionResult result = RenderEvents.RENDER_FOOD.invoke().render(this.getCameraPlayer(), guiGraphics);
        if (result == InteractionResult.FAIL) {
            ci.cancel();
        }
    }

    @Inject(method = "renderVehicleHealth", at = @At(value = "HEAD"), cancellable = true)
    private void shouldRenderMountHealth(GuiGraphics guiGraphics, CallbackInfo ci) {
        InteractionResult result = RenderEvents.RENDER_MOUNT_HEALTH.invoke().render(this.getCameraPlayer(), guiGraphics);
        if (result == InteractionResult.FAIL) {
            ci.cancel();
        }
    }
}
