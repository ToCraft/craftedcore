package tocraft.craftedcore.mixin;

import net.minecraft.advancements.Advancement;
//#if MC>1201
import net.minecraft.advancements.AdvancementHolder;
//#endif
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tocraft.craftedcore.event.common.PlayerEvents;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {
    @Shadow
    private ServerPlayer player;

    @Inject(method = "award", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/AdvancementRewards;grant(Lnet/minecraft/server/level/ServerPlayer;)V"))
    //#if MC>1201
    private void afterAward(AdvancementHolder advancement, String criterionKey, CallbackInfoReturnable<Boolean> cir) {
    //#else
    //$$ private void afterAward(Advancement advancement, String criterionKey, CallbackInfoReturnable<Boolean> cir) {
    //#endif
        PlayerEvents.AWARD_ADVANCEMENT.invoke().award(this.player, advancement, criterionKey);
    }

    @Inject(method = "revoke", at = @At(value = "RETURN"))
    //#if MC>1201
    private void afterRevoke(AdvancementHolder advancement, String criterionKey, CallbackInfoReturnable<Boolean> cir) {
    //#else
    //$$ private void afterRevoke(Advancement advancement, String criterionKey, CallbackInfoReturnable<Boolean> cir) {
    //#endif
        PlayerEvents.REVOKE_ADVANCEMENT.invoke().revoke(this.player, advancement, criterionKey);
    }
}
