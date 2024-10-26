package tocraft.craftedcore.patched;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
//#if MC>1182
import net.minecraft.core.registries.BuiltInRegistries;
//#endif

@SuppressWarnings({"unused", "unchecked"})
public class CRegistries {
    //#if MC>=1212
    public static Registry<?> getRegistry(ResourceLocation id) {
        return BuiltInRegistries.REGISTRY.get(id).orElseThrow().value();
    }
    //#elseif MC>1182
    //$$ public static Registry<?> getRegistry(ResourceLocation id) {
    //$$     return BuiltInRegistries.REGISTRY.get(id);
    //$$ }
    //#else
    //$$ public static Registry<?> getRegistry(ResourceLocation id) {
    //$$     return Registry.REGISTRY.get(id);
    //$$ }
    //#endif
}
