package tocraft.craftedcore.registration;

import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;

@SuppressWarnings("unused")
public final class KeyBindingRegistry {
    public static void register(KeyMapping keyMapping) {
        KeyMappingRegistry.register(keyMapping);
    }
}
