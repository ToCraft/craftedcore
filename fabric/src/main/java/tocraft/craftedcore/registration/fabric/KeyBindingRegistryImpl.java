package tocraft.craftedcore.registration.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class KeyBindingRegistryImpl {
    public static void register(KeyMapping keyMapping) {
        KeyBindingHelper.registerKeyBinding(keyMapping);
    }
}
