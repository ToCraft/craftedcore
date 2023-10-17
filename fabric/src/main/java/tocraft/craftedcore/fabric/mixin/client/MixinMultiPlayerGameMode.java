package tocraft.craftedcore.fabric.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import tocraft.craftedcore.events.common.PlayerEvents;

@Mixin(MultiPlayerGameMode.class)
public class MixinMultiPlayerGameMode {
	@Inject(method = "interact",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V",
                    shift = At.Shift.AFTER),
            cancellable = true)
    private void entityInteract(Player player, Entity entity, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        var result = PlayerEvents.INTERACT_ENTITY.invoker().interact(player, entity, interactionHand);
        if (result.isPresent()) {
            cir.setReturnValue(result.asMinecraft());
        }
    }
}
