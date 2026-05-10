package dev.tocraft.craftedcore.registration.fabric;

import dev.tocraft.craftedcore.registration.RegistryRegistryService;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

@SuppressWarnings("unused")
public class RegistryRegistryImpl implements RegistryRegistryService {
    @Override
    public <T> Registry<T> createSimpleRegistry(ResourceKey<Registry<T>> key) {
        return FabricRegistryBuilder.create(key).buildAndRegister();
    }
}
