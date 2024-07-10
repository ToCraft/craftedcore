package tocraft.craftedcore.registration.forge;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.ArrayUtils;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class KeyBindingRegistryImpl {
    public static void register(KeyMapping keyMapping) {
        Options options = Minecraft.getInstance().options;
        options.keyMappings = ArrayUtils.add(options.keyMappings, keyMapping);
    }
}
