package tocraft.craftedcore.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tocraft.craftedcore.data.PlayerDataProvider;
import tocraft.craftedcore.event.common.EntityEvents;
import tocraft.craftedcore.registration.PlayerDataRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unused", "DataFlowIssue"})
@Mixin(Player.class)
public abstract class PlayerMixin implements PlayerDataProvider {
    @Unique
    private final Map<String, CompoundTag> craftedcore$playerData = new HashMap<>();

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void readNbt(ValueInput in, CallbackInfo ci) {
        for (String k : PlayerDataRegistry.keySet()) {
            craftedcore$playerData.put(k, in.read(k, CompoundTag.CODEC).orElse(new CompoundTag()));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void writeNbt(ValueOutput out, CallbackInfo ci) {
        craftedcore$playerData.forEach((k, v) -> {
            if (v != null && PlayerDataRegistry.isKeyRegistered(k)) {
                out.store(k, CompoundTag.CODEC, v);
            }
        });
    }

    @Inject(method = "interactOn", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0),
            cancellable = true)
    private void onInteraction(Entity entity, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        InteractionResult result = EntityEvents.INTERACT_WITH_PLAYER.invoke().interact((Player) (Object) this, entity, interactionHand);
        if (result != InteractionResult.PASS) {
            cir.setReturnValue(result);
        }
    }

    @Unique
    @Override
    public Set<String> craftedcore$keySet() {
        return craftedcore$playerData.keySet();
    }

    @Unique
    @Override
    public void craftedcore$writeTag(String key, CompoundTag tag) {
        craftedcore$playerData.put(key, tag);
    }

    @Unique
    @Override
    public CompoundTag craftedcore$readTag(String key) {
        return craftedcore$playerData.get(key);
    }
}
