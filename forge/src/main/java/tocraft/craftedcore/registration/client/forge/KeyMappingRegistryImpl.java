package tocraft.craftedcore.registration.client.forge;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

public class KeyMappingRegistryImpl {
    public static void register(KeyMapping mapping) {
        Options options = Minecraft.getInstance().options;
        options.keyMappings = ArrayUtils.add(options.keyMappings, mapping);
    }
}
