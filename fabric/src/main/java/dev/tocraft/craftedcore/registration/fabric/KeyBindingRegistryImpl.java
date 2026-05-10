package dev.tocraft.craftedcore.registration.fabric;

import dev.tocraft.craftedcore.registration.KeyBindingRegistryService;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class KeyBindingRegistryImpl implements KeyBindingRegistryService {
    @Override
    public void register(KeyMapping keyMapping) {
        KeyMappingHelper.registerKeyMapping(keyMapping);
    }
}
