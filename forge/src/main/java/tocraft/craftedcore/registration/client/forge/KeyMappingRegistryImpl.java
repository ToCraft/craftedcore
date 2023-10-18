package tocraft.craftedcore.registration.client.forge;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tocraft.craftedcore.CraftedCore;

public class KeyMappingRegistryImpl {
    private static final List<KeyMapping> MAPPINGS = new ArrayList<>();
    private static boolean eventCalled = false;
    
    public static void register(KeyMapping mapping) {
        if (eventCalled) {
            Options options = Minecraft.getInstance().options;
            options.keyMappings = ArrayUtils.add(options.keyMappings, mapping);
            CraftedCore.LOGGER.warn("Key mapping %s registered after event".formatted(mapping.getName()), new RuntimeException());
        } else {
            MAPPINGS.add(mapping);
        }
    }
    
    @SubscribeEvent
    public static void event(RegisterKeyMappingsEvent event) {
        MAPPINGS.forEach(event::register);
        eventCalled = true;
    }
}
