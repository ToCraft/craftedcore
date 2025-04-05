package tocraft.craftedcore.registration;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class RegistryRegistry {
    @ExpectPlatform
    public static <T> Registry<T> createSimpleRegistry(ResourceKey<Registry<T>> key) {
        throw new AssertionError();
    }

}
