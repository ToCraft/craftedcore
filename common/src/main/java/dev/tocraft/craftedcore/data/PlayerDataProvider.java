package dev.tocraft.craftedcore.data;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@ApiStatus.Internal
public interface PlayerDataProvider {
    <O> void craftedcore$writeTag(String key, @Nullable O value);

    Set<String> craftedcore$keySet();

    @Nullable <T> T craftedcore$readTag(String key, Class<T> type);

    @Nullable Object craftedcore$readTag(String key);
}