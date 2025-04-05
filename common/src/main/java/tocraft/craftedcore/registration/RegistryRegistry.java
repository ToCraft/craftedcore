package tocraft.craftedcore.registration;

import com.mojang.serialization.Codec;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;

public class RegistryRegistry {
    @ExpectPlatform
    public static <T> Registry<T> createSimpleRegistry(ResourceKey<Registry<T>> key) {
        throw new AssertionError();
    }

    public static void registerWorldgen(RegistryDataLoader.RegistryData<?> registryData) {
        RegistryDataLoader.WORLDGEN_REGISTRIES.add(registryData);
    }

    public static void registerDimension(RegistryDataLoader.RegistryData<?> registryData) {
        RegistryDataLoader.DIMENSION_REGISTRIES.add(registryData);
    }

    public static void registerSynchronized(RegistryDataLoader.RegistryData<?> registryData) {
        RegistryDataLoader.DIMENSION_REGISTRIES.add(registryData);
    }
}
