package tocraft.craftedcore.registration;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.world.entity.player.Player;
import tocraft.craftedcore.data.PlayerDataProvider;

public class PlayerDataRegistry {
	private static final Map<String, Map.Entry<Boolean, Boolean>> allKeys = new HashMap<String, Map.Entry<Boolean, Boolean>>();
	
	public static void registerKey(String key) {
		registerKey(key, false);
	}
	
	/** Should be called once the player joins and for every key
	 * 
	 * @param key your key
	 * @param persistent should the tag be restored after death?
	 */
	public static void registerKey(String key, boolean persistent) {
		registerKey(key, persistent, true);
	}
	
	/** Should be called once the player joins and for every key
	 * 
	 * @param key your key
	 * @param persistent should the tag be restored after death?
	 * @param sync should the tag be synchronized to the client?
	 */
	public static void registerKey(String key, boolean persistent, boolean sync) {
		allKeys.put(key, new SimpleEntry<Boolean, Boolean>(persistent, sync));
	}
	
	public static List<String> getAllKeys() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 3218806923808497702L;

			{
				addAll(allKeys.keySet());
			}
		};
	}
	
	public static boolean isKeyPersistant(String key) {
		return allKeys.containsKey(key) && allKeys.get(key).getKey();
	}
	
	public static boolean shouldKeySync(String key) {
		return allKeys.containsKey(key) && allKeys.get(key).getValue();
	}
	
	public static PlayerDataProvider getPlayerDataProvider(Player player) {
		return (PlayerDataProvider) player;
	}
}
