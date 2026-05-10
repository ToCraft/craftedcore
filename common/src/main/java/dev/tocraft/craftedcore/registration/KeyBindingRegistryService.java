package dev.tocraft.craftedcore.registration;

import net.minecraft.client.KeyMapping;

public interface KeyBindingRegistryService {
    void register(KeyMapping keyMapping);
}
