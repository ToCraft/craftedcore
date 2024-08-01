package tocraft.craftedcore.registration;

import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import tocraft.craftedcore.data.PlayerDataProvider;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class PlayerDataRegistry {
    private static final Map<String, Map.Entry<Boolean, Boolean>> CraftedTagKeys = new HashMap<>();

    /**
     * Should be called once the player joins and for every key
     *
     * @param key        your key
     * @param persistent should the tag be restored after death?
     */
    public static void registerKey(String key, boolean persistent) {
        registerKey(key, persistent, true);
    }

    public static boolean isKeyRegistered(String key) {
        return CraftedTagKeys.containsKey(key);
    }

    /**
     * Should be called once the player joins and for every key
     *
     * @param key        your key
     * @param persistent should the tag be restored after death?
     * @param sync       should the tag be synchronized to the client?
     */
    public static void registerKey(String key, boolean persistent, boolean sync) {
        CraftedTagKeys.put(key, new SimpleEntry<>(persistent, sync));
    }

    public static boolean isKeyPersistent(String key) {
        return CraftedTagKeys.containsKey(key) && CraftedTagKeys.get(key).getKey();
    }

    public static boolean shouldSyncKey(String key) {
        return CraftedTagKeys.containsKey(key) && CraftedTagKeys.get(key).getValue();
    }

    public static void writeTag(Player player, String key, Tag value) throws NotRegisteredTagKeyException {
        PlayerDataProvider playerDataProvider = (PlayerDataProvider) player;
        if (isKeyRegistered(key)) {
            playerDataProvider.craftedcore$writeTag(key, value);
        } else {
            throw new NotRegisteredTagKeyException(key);
        }
    }

    @Nullable
    public static Tag readTag(Player player, String key) throws NotRegisteredTagKeyException {
        PlayerDataProvider playerDataProvider = (PlayerDataProvider) player;
        if (isKeyRegistered(key)) {
            return playerDataProvider.craftedcore$readTag(key);
        } else {
            throw new NotRegisteredTagKeyException(key);
        }
    }

    public static class NotRegisteredTagKeyException extends IllegalArgumentException {
        public NotRegisteredTagKeyException(String key) {
            super("Player Data Key " + key + " not found!");
        }
    }
}
