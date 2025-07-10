package dev.tocraft.craftedcore.registration;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import dev.tocraft.craftedcore.data.PlayerDataProvider;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class PlayerDataRegistry {
    private static final Map<String, TagData<?>> CRAFTED_TAG_KEYS = new HashMap<>();

    /**
     * Should be called once the player joins and for every key
     *
     * @param key        your key
     * @param persistent should the tag be restored after death?
     */
    public static <O> void registerKey(String key, Codec<O> codec, boolean persistent) {
        registerKey(key, codec, persistent, true);
    }

    public static boolean isKeyRegistered(String key) {
        return CRAFTED_TAG_KEYS.containsKey(key);
    }

    /**
     * Should be called once the player joins and for every key
     *
     * @param key        your key
     * @param persistent should the tag be restored after death?
     * @param sync       should the tag be synchronized to the client?
     */
    public static <O> void registerKey(String key, Codec<O> codec, boolean persistent, boolean sync) {
        registerKey(key, codec, persistent, sync, sync);
    }

    public static <O> void registerKey(String key, Codec<O> codec, boolean persistent, boolean syncToSelf, boolean syncToOthers) {
        CRAFTED_TAG_KEYS.put(key, new TagData<>(codec, persistent, syncToSelf, syncToOthers));
    }

    public static boolean isKeyPersistent(String key) {
        return CRAFTED_TAG_KEYS.containsKey(key) && CRAFTED_TAG_KEYS.get(key).persistent();
    }

    @SuppressWarnings("unchecked")
    public static <O> @Nullable Codec<O> getTagCodec(String key) {
        TagData<?> data = CRAFTED_TAG_KEYS.get(key);
        return data != null ? (Codec<O>) data.codec() : null;
    }

    public static boolean shouldSyncKey(String key) {
        return shouldSyncTagToSelf(key) && shouldSyncTagToAll(key);
    }

    public static boolean shouldSyncTagToSelf(String key) {
        return CRAFTED_TAG_KEYS.containsKey(key) && CRAFTED_TAG_KEYS.get(key).syncToSelf();
    }

    public static boolean shouldSyncTagToAll(String key) {
        return CRAFTED_TAG_KEYS.containsKey(key) && CRAFTED_TAG_KEYS.get(key).syncToAll();
    }

    public static <T> void writeTag(Player player, String key, @Nullable T value) throws NotRegisteredTagKeyException {
        PlayerDataProvider playerDataProvider = (PlayerDataProvider) player;
        if (isKeyRegistered(key)) {
            playerDataProvider.craftedcore$writeTag(key, value);
        } else {
            throw new NotRegisteredTagKeyException(key);
        }
    }

    public static <T> @Nullable T readTag(Player player, String key, Class<T> type) throws NotRegisteredTagKeyException {
        PlayerDataProvider playerDataProvider = (PlayerDataProvider) player;
        if (isKeyRegistered(key)) {
            return playerDataProvider.craftedcore$readTag(key, type);
        } else {
            throw new NotRegisteredTagKeyException(key);
        }
    }

    // Kept for backward compatibility, though discouraged
    public static Object readTag(Player player, String key) throws NotRegisteredTagKeyException {
        PlayerDataProvider playerDataProvider = (PlayerDataProvider) player;
        if (isKeyRegistered(key)) {
            return playerDataProvider.craftedcore$readTag(key);
        } else {
            throw new NotRegisteredTagKeyException(key);
        }
    }

    @Contract(pure = true)
    public static @NotNull Set<String> keySet() {
        return CRAFTED_TAG_KEYS.keySet();
    }

    public static class NotRegisteredTagKeyException extends IllegalArgumentException {
        public NotRegisteredTagKeyException(String key) {
            super("Player Data Key " + key + " not found!");
        }
    }

    private record TagData<O>(Codec<O> codec, boolean persistent, boolean syncToSelf, boolean syncToAll) {

    }
}