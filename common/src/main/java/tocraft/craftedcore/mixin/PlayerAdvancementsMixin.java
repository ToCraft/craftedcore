package tocraft.craftedcore.mixin;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.PlayerAdvancements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tocraft.craftedcore.event.common.PlayerEvents;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {
    @Inject(method = "award", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/AdvancementRewards;grant(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void afterAward(AdvancementHolder advancement, String criterionKey, CallbackInfoReturnable<Boolean> cir) {
        PlayerEvents.AWARD_ADVANCEMENT.invoke().award(advancement, criterionKey);
    }

    @Inject(method = "revoke", at = @At(value = "RETURN"))
    private void afterRevoke(AdvancementHolder advancement, String criterionKey, CallbackInfoReturnable<Boolean> cir) {
        PlayerEvents.REVOKE_ADVANCEMENT.invoke().revoke(advancement, criterionKey);
    }
}
