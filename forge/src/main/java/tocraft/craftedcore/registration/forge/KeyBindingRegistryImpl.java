package tocraft.craftedcore.registration.forge;

import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class KeyBindingRegistryImpl {
    private static final List<KeyMapping> KEY_MAPPINGS = new ArrayList<>();
    private static boolean eventAlreadyTriggered = false;

    public static void register(KeyMapping keyMapping) {
        if (!eventAlreadyTriggered) {
            KEY_MAPPINGS.add(keyMapping);
        } else {
            Options options = Minecraft.getInstance().options;
            options.keyMappings = ArrayUtils.add(options.keyMappings, keyMapping);
            LogUtils.getLogger().warn("Key Registration Event already run for key {}.", keyMapping.getName());
        }
    }

    public static List<KeyMapping> getMappingsForEvent() {
        eventAlreadyTriggered = true;
        return new ArrayList<>(KEY_MAPPINGS);
    }
}