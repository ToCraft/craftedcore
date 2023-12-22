package tocraft.craftedcore.data;

import java.util.Set;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public interface PlayerDataProvider {
	void writeTag(String key, Tag value);
	
	Set<String> keySet();
		
	Tag readTag(String key);
	
	CompoundTag readPlayerDataTagCompound(String key);
	
	ListTag readPlayerDataTagList(String key);
}
