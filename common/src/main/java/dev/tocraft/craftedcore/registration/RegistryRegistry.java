package dev.tocraft.craftedcore.registration;

import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

import java.util.ServiceLoader;

@SuppressWarnings("unused")
public class RegistryRegistry {
    private static final RegistryRegistryService SERVICE =
            ServiceLoader.load(RegistryRegistryService.class).findFirst().orElseThrow();

    public static <T> Registry<@NotNull T> createSimpleRegistry(ResourceKey<@NotNull Registry<@NotNull T>> key) {
        return SERVICE.createSimpleRegistry(key);
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static void registerWorldgen(RegistryDataLoader.RegistryData<?> registryData) {
        RegistryDataLoader.WORLD_REGISTRIES.add(registryData);
    }

    public static void registerDimension(RegistryDataLoader.RegistryData<?> registryData) {
        RegistryDataLoader.DIMENSION_REGISTRIES.add(registryData);
    }

    public static void registerSynchronized(RegistryDataLoader.RegistryData<?> registryData) {
        RegistryDataLoader.DIMENSION_REGISTRIES.add(registryData);
    }
}
