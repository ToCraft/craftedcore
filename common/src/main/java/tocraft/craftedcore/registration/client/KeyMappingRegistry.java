package tocraft.craftedcore.registration.client;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.KeyMapping;

public final class KeyMappingRegistry {
	@ExpectPlatform
    public static void register(KeyMapping mapping) {
		throw new AssertionError();
    }
}
