package tocraft.craftedcore.forge.registration.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.registration.client.KeyMappingRegistry;

@Mod.EventBusSubscriber(modid = CraftedCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeRegistryHandlerClient {
	
    @SubscribeEvent
    public static void KeyMappingRegistrationEvent(RegisterKeyMappingsEvent event) {
        KeyMappingRegistry.MAPPINGS.forEach(event::register);
    }
}
