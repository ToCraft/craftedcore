package tocraft.craftedcore.patched;

import net.minecraft.resources.ResourceLocation;

public class Identifier {
    public static ResourceLocation parse(String namespace, String path) {
        //#if MC>=1210
        //$$ return ResourceLocation.fromNamespaceAndPath(namespace, path);
        //#else
        return new ResourceLocation(namespace, path);
        //#endif
    }

    public static ResourceLocation parse(String key) {
        //#if MC>=1210
        //$$ return ResourceLocation.parse(key);
        //#else
        return new ResourceLocation(key);
        //#endif
    }
}
