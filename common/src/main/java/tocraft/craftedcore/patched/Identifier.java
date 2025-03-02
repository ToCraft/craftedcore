package tocraft.craftedcore.patched;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Identifier {
    @Contract("_, _ -> new")
    public static @NotNull ResourceLocation parse(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static @NotNull ResourceLocation parse(String key) {
        return ResourceLocation.parse(key);
    }
}
