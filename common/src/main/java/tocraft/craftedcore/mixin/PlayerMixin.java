package tocraft.craftedcore.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.data.PlayerDataProvider;
import tocraft.craftedcore.registration.PlayerDataRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Mixin(Player.class)
public abstract class PlayerMixin implements PlayerDataProvider {
    @Unique
    private final Map<String, Tag> craftedcore$playerData = new HashMap<String, Tag>();

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void readNbt(CompoundTag tag, CallbackInfo info) {
        for (String key : PlayerDataRegistry.keySet()) {
            craftedcore$playerData.put(key, tag.get(key));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void writeNbt(CompoundTag tag, CallbackInfo info) {
        craftedcore$playerData.forEach((key, value) -> tag.put(key, this.craftedcore$readTag(key)));
    }

    @Unique
    @Override
    public Set<String> craftedcore$keySet() {
        return craftedcore$playerData.keySet();
    }

    @Unique
    @Override
    public void craftedcore$writeTag(String key, Tag tag) {
        if (!PlayerDataRegistry.keySet().contains(key))
            PlayerDataRegistry.registerKey(key, false);

        craftedcore$playerData.put(key, tag);
    }

    @Unique
    @Override
    public Tag craftedcore$readTag(String key) {
        return craftedcore$playerData.get(key);
    }
}
