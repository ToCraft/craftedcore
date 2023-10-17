package tocraft.craftedcore.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import tocraft.craftedcore.events.common.PlayerEvents;

@Mixin(Player.class)
public class MixinPlayer {
	@Inject(method = "interactOn", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0),
            cancellable = true)
    private void entityInteract(Entity entity, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        var result = PlayerEvents.INTERACT_ENTITY.invoker().interact((Player) (Object) this, entity, interactionHand);
        if (result.isPresent()) {
            cir.setReturnValue(result.asMinecraft());
        }
    }
}
