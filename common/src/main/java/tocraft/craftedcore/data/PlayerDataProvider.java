package tocraft.craftedcore.data;

import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@ApiStatus.Internal
public interface PlayerDataProvider {
    void craftedcore$writeTag(String key, Tag value);

    Set<String> craftedcore$keySet();

    Tag craftedcore$readTag(String key);
}
