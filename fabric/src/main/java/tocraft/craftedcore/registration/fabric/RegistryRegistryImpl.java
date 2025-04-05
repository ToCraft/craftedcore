package tocraft.craftedcore.registration.fabric;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

@SuppressWarnings("unused")
public class RegistryRegistryImpl {
    public static <T> Registry<T> createSimpleRegistry(ResourceKey<Registry<T>> key) {
        return FabricRegistryBuilder.createSimple(key).buildAndRegister();
    }
}
