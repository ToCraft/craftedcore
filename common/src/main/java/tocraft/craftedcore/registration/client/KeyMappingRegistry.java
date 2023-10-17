package tocraft.craftedcore.registration.client;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;

@Environment(EnvType.CLIENT)
public final class KeyMappingRegistry {
	public static final List<KeyMapping> MAPPINGS = new ArrayList<>();
	
	/**
	 *  MUST be called before Forge's RegisterKeyMappingsEvent, otherwise, nothing will happen
	 */
	public static void register(KeyMapping mapping) {
		MAPPINGS.add(mapping);
    }
}
