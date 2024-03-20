package tocraft.craftedcore.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.Set;

public interface PlayerDataProvider {
    void craftedcore$writeTag(String key, Tag value);

    Set<String> craftedcore$keySet();

    Tag craftedcore$readTag(String key);
}
