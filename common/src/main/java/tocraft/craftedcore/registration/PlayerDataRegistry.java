package tocraft.craftedcore.registration;

import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tocraft.craftedcore.data.PlayerDataProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class PlayerDataRegistry {
    private static final Map<String, TagData> CraftedTagKeys = new HashMap<>();

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
        registerKey(key, persistent, sync, sync);
    }

    public static void registerKey(String key, boolean persistent, boolean syncToSelf, boolean syncToOthers) {
        CraftedTagKeys.put(key, new TagData(persistent, syncToSelf, syncToOthers));
    }

    public static boolean isKeyPersistent(String key) {
        return CraftedTagKeys.containsKey(key) && CraftedTagKeys.get(key).persistent();
    }

    public static boolean shouldSyncKey(String key) {
        return shouldSyncTagToSelf(key) && shouldSyncTagToAll(key);
    }

    public static boolean shouldSyncTagToSelf(String key) {
        return CraftedTagKeys.containsKey(key) && CraftedTagKeys.get(key).syncToSelf();
    }

    public static boolean shouldSyncTagToAll(String key) {
        return CraftedTagKeys.containsKey(key) && CraftedTagKeys.get(key).syncToAll();
    }

    public static void writeTag(Player player, String key, @NotNull Tag value) throws NotRegisteredTagKeyException {
        PlayerDataProvider playerDataProvider = (PlayerDataProvider) player;
        if (isKeyRegistered(key)) {
            playerDataProvider.craftedcore$writeTag(key, value);
        } else {
            throw new NotRegisteredTagKeyException(key);
        }
    }

    public static Tag readTag(Player player, String key) throws NotRegisteredTagKeyException {
        PlayerDataProvider playerDataProvider = (PlayerDataProvider) player;
        if (isKeyRegistered(key)) {
            return playerDataProvider.craftedcore$readTag(key);
        } else {
            throw new NotRegisteredTagKeyException(key);
        }
    }

    public static Set<String> keySet() {
        return CraftedTagKeys.keySet();
    }

    public static class NotRegisteredTagKeyException extends IllegalArgumentException {
        public NotRegisteredTagKeyException(String key) {
            super("Player Data Key " + key + " not found!");
        }
    }

    private record TagData(boolean persistent, boolean syncToSelf, boolean syncToAll) {

    }
}
