package dev.tocraft.craftedcore.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import dev.tocraft.craftedcore.client.CraftedCoreClient;
import dev.tocraft.craftedcore.registration.neoforge.KeyBindingRegistryImpl;

@OnlyIn(Dist.CLIENT)
public class CraftedCoreNeoForgeClient {
    public CraftedCoreNeoForgeClient(IEventBus bus) {
        NeoForge.EVENT_BUS.register(new CraftedCoreNeoForgeEventHandlerClient());
        bus.addListener(KeyBindingRegistryImpl::event);

        new CraftedCoreClient().initialize();
    }
}
