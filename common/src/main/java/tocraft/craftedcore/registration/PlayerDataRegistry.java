package tocraft.craftedcore.registration;

import net.minecraft.world.entity.player.Player;
import tocraft.craftedcore.data.PlayerDataProvider;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlayerDataRegistry {
    private static final Map<String, Map.Entry<Boolean, Boolean>> CraftedTagKeys = new HashMap<String, Map.Entry<Boolean, Boolean>>();

    /**
     * Should be called once the player joins and for every key
     *
     * @param key        your key
     * @param persistent should the tag be restored after death?
     */
    public static void registerKey(String key, boolean persistent) {
        registerKey(key, persistent, true);
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

    public static Set<String> keySet() {
        return CraftedTagKeys.keySet();
    }

    public static boolean isKeyPersistent(String key) {
        return CraftedTagKeys.containsKey(key) && CraftedTagKeys.get(key).getKey();
    }

    public static boolean shouldSyncKey(String key) {
        return CraftedTagKeys.containsKey(key) && CraftedTagKeys.get(key).getValue();
    }

    public static PlayerDataProvider getPlayerDataProvider(Player player) {
        return (PlayerDataProvider) player;
    }
}
