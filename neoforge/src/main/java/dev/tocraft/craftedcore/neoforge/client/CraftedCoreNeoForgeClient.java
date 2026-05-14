package dev.tocraft.craftedcore.neoforge.client;

import dev.tocraft.craftedcore.client.CraftedCoreClient;
import dev.tocraft.craftedcore.registration.neoforge.KeyBindingRegistryImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

@Environment(EnvType.CLIENT)
public class CraftedCoreNeoForgeClient {
    public CraftedCoreNeoForgeClient(IEventBus bus) {
        NeoForge.EVENT_BUS.register(new CraftedCoreNeoForgeEventHandlerClient());
        bus.addListener(KeyBindingRegistryImpl::event);

        new CraftedCoreClient().initialize();
    }
}
