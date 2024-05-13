package tocraft.craftedcore.neoforge;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.SleepingTimeCheckEvent;
import net.neoforged.neoforge.event.level.SleepFinishedTimeEvent;
import tocraft.craftedcore.data.SynchronizedJsonReloadListener;
import tocraft.craftedcore.event.common.CommandEvents;
import tocraft.craftedcore.event.common.EntityEvents;
import tocraft.craftedcore.event.common.PlayerEvents;
import tocraft.craftedcore.registration.SynchronizedReloadListenerRegistry;

@SuppressWarnings("unused")
public class CraftedCoreNeoForgeEventHandler {
    @SubscribeEvent
    public void addReloadListenerEvent(AddReloadListenerEvent event) {
        for (SynchronizedJsonReloadListener lister : SynchronizedReloadListenerRegistry.getAllListener().values()) {
            event.addListener(lister);
        }
    }

    @SubscribeEvent
    public void livingDeath(LivingDeathEvent event) {
        InteractionResult result = EntityEvents.LIVING_DEATH.invoke().die(event.getEntity(), event.getSource());
        if (result == InteractionResult.FAIL) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void allowSleepTime(SleepingTimeCheckEvent event) {
        InteractionResult result = PlayerEvents.ALLOW_SLEEP_TIME.invoke().allowSleepTime(event.getEntity(), event.getSleepingLocation().isPresent() ? event.getSleepingLocation().get() : null, event.getResult() != Event.Result.DENY);
        if (result == InteractionResult.FAIL) {
            event.setResult(Event.Result.DENY);
        }
        if (result == InteractionResult.SUCCESS) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public void sleepFinishedTime(SleepFinishedTimeEvent event) {
        long newTimeIn = PlayerEvents.SLEEP_FINISHED_TIME.invoke().setTimeAddition((ServerLevel) event.getLevel(), event.getNewTime());
        event.setTimeAddition(newTimeIn);
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandEvents.REGISTRATION.invoke().register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }
}
