package dev.tocraft.craftedcore.registration.neoforge;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RegistryRegistryImpl {
    private static final List<Registry<?>> registries = new ArrayList<>();

    public static <T> @NotNull Registry<T> createSimpleRegistry(ResourceKey<Registry<T>> key) {
        var registry = new RegistryBuilder<>(key).create();
        registries.add(registry);
        return registry;
    }

    @Contract(value = " -> new", pure = true)
    @ApiStatus.Internal
    public static @NotNull List<Registry<?>> getRegistries() {
        return new ArrayList<>(registries);
    }
}
