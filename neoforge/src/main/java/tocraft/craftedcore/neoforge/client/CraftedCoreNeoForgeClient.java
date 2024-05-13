package tocraft.craftedcore.neoforge.client;

import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import tocraft.craftedcore.client.CraftedCoreClient;
import tocraft.craftedcore.registration.neoforge.KeyBindingRegistryImpl;

@OnlyIn(Dist.CLIENT)
public class CraftedCoreNeoForgeClient {
    public CraftedCoreNeoForgeClient(IEventBus bus) {
        NeoForge.EVENT_BUS.register(new CraftedCoreNeoForgeEventHandlerClient());

        new CraftedCoreClient().initialize();

        bus.addListener(RegisterKeyMappingsEvent.class, event -> {
            for (KeyMapping mapping : KeyBindingRegistryImpl.getMappingsForEvent()) {
                event.register(mapping);
            }
        });
    }
}
