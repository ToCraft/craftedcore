package tocraft.craftedcore.registration.neoforge;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.ArrayUtils;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class KeyBindingRegistryImpl {
    public static void register(KeyMapping keyMapping) {
        Minecraft mc = Minecraft.getInstance();

        //noinspection ConstantValue
        if (mc != null) {
            Options options = mc.options;
            options.keyMappings = ArrayUtils.add(options.keyMappings, keyMapping);
        }
    }
}
