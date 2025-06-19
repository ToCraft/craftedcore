package tocraft.craftedcore.registration.neoforge;

import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class KeyBindingRegistryImpl {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<KeyMapping> MAPPINGS = new ArrayList<>();
    private static boolean eventCalled = false;

    public static void register(KeyMapping mapping) {
        if (eventCalled) {
            Options options = Minecraft.getInstance().options;
            options.keyMappings = ArrayUtils.add(options.keyMappings, mapping);
            LOGGER.warn("Key mapping {} registered after event", mapping.getName(), new RuntimeException());
        } else {
            MAPPINGS.add(mapping);
        }
    }

    public static void event(@NotNull RegisterKeyMappingsEvent event) {
        MAPPINGS.forEach(event::register);
        eventCalled = true;
    }
}
