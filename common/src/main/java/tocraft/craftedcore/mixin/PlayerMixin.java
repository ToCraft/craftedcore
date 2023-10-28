package tocraft.craftedcore.mixin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import tocraft.craftedcore.data.PlayerDataProvider;
import tocraft.craftedcore.registration.PlayerDataRegistry;

@Mixin(Player.class)
public abstract class PlayerMixin implements PlayerDataProvider {
	private final Map<String, Tag> playerData = new HashMap<String, Tag>();
	
	@Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
	private void readNbt(CompoundTag tag, CallbackInfo info) {
		PlayerDataRegistry.getAllKeys().forEach(key -> {
			playerData.put(key, tag.getCompound(key));
		});
	}

	@Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
	private void writeNbt(CompoundTag tag, CallbackInfo info) {
		playerData.forEach((key, value) -> {
			tag.put(key, ((PlayerDataProvider) (Object) this).readPlayerDataTag(key));
		});
	}
	
	@Unique
	@Override
	public boolean containsKey(String key) {
		return playerData.containsKey(key);
	}
	
	@Unique
	@Override
	public void writePlayerData(String key, Tag tag) {
		if (!PlayerDataRegistry.getAllKeys().contains(key))
			PlayerDataRegistry.registerKey(key);
		
		playerData.put(key, tag);
	}
	
	@Unique
	@Override
	public Tag readPlayerDataTag(String key) {
		return playerData.get(key);
	}
	
	@Unique
	@Override
	public CompoundTag readPlayerDataTagCompound(String key) {
		if (playerData.get(key) instanceof CompoundTag)
			return (CompoundTag) playerData.get(key);
		else
			return new CompoundTag();
	}
	
	@Unique
	@Override
	public ListTag readPlayerDataTagList(String key) {
		if (playerData.get(key) instanceof ListTag)
			return (ListTag) playerData.get(key);
		else
			return new ListTag();
	}
	
	@Unique
	@Override
	public void foreachKeyAndValue(BiConsumer<String, Tag> action) {
		playerData.forEach(action);
	}
}
