package tocraft.craftedcore.forge;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tocraft.craftedcore.data.SynchronizedJsonReloadListener;
import tocraft.craftedcore.event.common.EntityEvents;
import tocraft.craftedcore.registration.SynchronizedReloadListenerRegistry;

@SuppressWarnings("unused")
public class CraftedCoreForgeEventHandler {
    @SubscribeEvent
    public void addReloadListenerEvent(AddReloadListenerEvent event) {
        for (SynchronizedJsonReloadListener lister : SynchronizedReloadListenerRegistry.getAllListener()) {
            event.addListener(lister);
        }
    }

    @SubscribeEvent
    public void livingDeath(LivingDeathEvent event) {
        InteractionResult result = EntityEvents.LIVING_DEATH.invoke().die((LivingEntity) event.getEntity(), event.getSource());
        if (result == InteractionResult.FAIL) {
            event.setCanceled(true);
        }
    }
}
