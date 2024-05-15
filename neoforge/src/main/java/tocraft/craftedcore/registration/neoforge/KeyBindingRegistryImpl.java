package tocraft.craftedcore.registration.neoforge;

import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

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
            LogUtils.getLogger().warn("Key Registration Event already run. Couldn't register Key {}", keyMapping.getName());
        }
    }

    public static List<KeyMapping> getMappingsForEvent() {
        eventAlreadyTriggered = true;
        return new ArrayList<>(KEY_MAPPINGS);
    }
}