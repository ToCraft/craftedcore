package dev.tocraft.craftedcore.registration;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class KeyBindingRegistry {
    @ExpectPlatform
    public static void register(KeyMapping keyMapping) {
        throw new AssertionError();
    }
}
