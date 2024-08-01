package tocraft.craftedcore.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
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
    private final Map<String, Tag> craftedcore$playerData = new HashMap<>();

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void readNbt(CompoundTag tag, CallbackInfo info) {
        for (String k : PlayerDataRegistry.keySet()) {
            craftedcore$playerData.put(k, tag.get(k));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void writeNbt(CompoundTag tag, CallbackInfo info) {
        craftedcore$playerData.forEach(tag::put);
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
    public void craftedcore$writeTag(String key, Tag tag) {
        craftedcore$playerData.put(key, tag);
    }

    @Unique
    @Override
    public Tag craftedcore$readTag(String key) {
        return craftedcore$playerData.get(key);
    }
}
