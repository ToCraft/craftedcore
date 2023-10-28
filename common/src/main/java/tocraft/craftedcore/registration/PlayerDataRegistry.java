package tocraft.craftedcore.registration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDataRegistry {
	public static final Map<String, Boolean> allKeys = new HashMap<String, Boolean>();
	
	public static void registerKey(String key) {
		allKeys.put(key, false);
	}
	
	/** Should be called once the player joins and for every key
	 * 
	 * @param key your key
	 * @param persistent should the tag be restored after death?
	 */
	public static void registerKey(String key, boolean persistent) {
		allKeys.put(key, persistent);
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
		return allKeys.containsKey(key) && allKeys.get(key);
	}
}
