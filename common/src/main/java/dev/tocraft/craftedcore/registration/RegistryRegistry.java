package dev.tocraft.craftedcore.registration;

import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;

import java.util.ServiceLoader;

public class RegistryRegistry {
    private static final RegistryRegistryService SERVICE =
            ServiceLoader.load(RegistryRegistryService.class).findFirst().orElseThrow();

    public static <T> Registry<T> createSimpleRegistry(ResourceKey<Registry<T>> key) {
        return SERVICE.createSimpleRegistry(key);
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
