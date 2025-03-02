package tocraft.craftedcore.patched;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings({"unused"})
public class CRegistries {
    public static Registry<?> getRegistry(ResourceLocation id) {
        return BuiltInRegistries.REGISTRY.get(id).orElseThrow().value();
    }
}
