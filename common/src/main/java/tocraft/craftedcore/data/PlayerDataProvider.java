package tocraft.craftedcore.data;

import java.util.function.BiConsumer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public interface PlayerDataProvider {
	void writePlayerData(String key, Tag value);
	
	boolean containsKey(String key);
		
	Tag readPlayerDataTag(String key);
	
	CompoundTag readPlayerDataTagCompound(String key);
	
	ListTag readPlayerDataTagList(String key);
	
	void foreachKeyAndValue(BiConsumer<String, Tag> action);
}
