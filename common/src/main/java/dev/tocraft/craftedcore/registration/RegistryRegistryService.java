package dev.tocraft.craftedcore.registration;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface RegistryRegistryService {
    <T> Registry<T> createSimpleRegistry(ResourceKey<Registry<T>> key);
}
