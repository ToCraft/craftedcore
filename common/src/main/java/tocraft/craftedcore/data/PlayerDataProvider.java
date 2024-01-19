package tocraft.craftedcore.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.Set;

public interface PlayerDataProvider {
    void writeTag(String key, Tag value);

    Set<String> keySet();

    Tag readTag(String key);

    CompoundTag readPlayerDataTagCompound(String key);

    ListTag readPlayerDataTagList(String key);
}
