package tocraft.craftedcore.fabric.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.event.client.RenderEvents;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    protected abstract Player getCameraPlayer();

    @ModifyExpressionValue(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean shouldRenderBreath(boolean isEyeInFluid) {
        InteractionResult result = RenderEvents.RENDER_BREATH.invoke().render(null, this.getCameraPlayer());
        if (result == InteractionResult.FAIL) {
            return false;
        } else {
            return isEyeInFluid;
        }
    }

    @Inject(method = "renderHearts", at = @At(value = "HEAD"), cancellable = true)
    private void shouldRenderHealth(PoseStack guiGraphics, Player player, int x, int y, int height, int offsetHeartIndex, float maxHealth, int currentHealth, int displayHealth, int absorptionAmount, boolean renderHighlight, CallbackInfo ci) {
        InteractionResult result = RenderEvents.RENDER_HEALTH.invoke().render(guiGraphics, null);
        if (result == InteractionResult.FAIL) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"))
    private int shouldRenderFood(int health) {
        InteractionResult result = RenderEvents.RENDER_FOOD.invoke().render(null, Minecraft.getInstance().player);
        if (result == InteractionResult.FAIL) {
            return -1;
        }
        return health;
    }

    @Inject(method = "renderVehicleHealth", at = @At(value = "HEAD"), cancellable = true)
    private void shouldRenderMountHealth(PoseStack guiGraphics, CallbackInfo ci) {
        InteractionResult result = RenderEvents.RENDER_MOUNT_HEALTH.invoke().render(guiGraphics, null);
        if (result == InteractionResult.FAIL) {
            ci.cancel();
        }
    }
}
