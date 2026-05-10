package dev.tocraft.craftedcore.registration;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;

import java.util.ServiceLoader;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class KeyBindingRegistry {
    private static final KeyBindingRegistryService SERVICE =
            ServiceLoader.load(KeyBindingRegistryService.class).findFirst().orElseThrow();

    public static void register(KeyMapping keyMapping) {
        SERVICE.register(keyMapping);
    }
}
