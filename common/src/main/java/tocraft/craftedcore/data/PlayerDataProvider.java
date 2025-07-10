package tocraft.craftedcore.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.ApiStatus;

import java.util.Set;

@ApiStatus.Internal
public interface PlayerDataProvider {
    void craftedcore$writeTag(String key, CompoundTag value);

    Set<String> craftedcore$keySet();

    CompoundTag craftedcore$readTag(String key);
}
