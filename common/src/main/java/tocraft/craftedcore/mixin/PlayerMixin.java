package tocraft.craftedcore.mixin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import tocraft.craftedcore.data.PlayerDataProvider;
import tocraft.craftedcore.registration.PlayerDataRegistry;

@Mixin(Player.class)
public abstract class PlayerMixin implements PlayerDataProvider {
	private final Map<String, Tag> playerData = new HashMap<String, Tag>();
	
	@Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
	private void readNbt(CompoundTag tag, CallbackInfo info) {
		PlayerDataRegistry.keySet().forEach(key -> {
			playerData.put(key, tag.getCompound(key));
		});
	}

	@Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
	private void writeNbt(CompoundTag tag, CallbackInfo info) {
		playerData.forEach((key, value) -> {
			tag.put(key, this.readTag(key));
		});
	}
	
	@Unique
	@Override
	public Set<String> keySet() {
		return playerData.keySet();
	}
	
	@Unique
	@Override
	public void writeTag(String key, Tag tag) {
		if (!PlayerDataRegistry.keySet().contains(key))
			PlayerDataRegistry.registerKey(key, false);
		
		playerData.put(key, tag);
	}
	
	@Unique
	@Override
	public Tag readTag(String key) {
		return playerData.get(key);
	}
}
