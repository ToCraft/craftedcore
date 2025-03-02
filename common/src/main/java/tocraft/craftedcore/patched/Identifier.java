package tocraft.craftedcore.patched;

import net.minecraft.resources.ResourceLocation;

public class Identifier {
    public static ResourceLocation parse(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static ResourceLocation parse(String key) {
        return ResourceLocation.parse(key);
    }
}
