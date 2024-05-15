package tocraft.craftedcore.forge;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tocraft.craftedcore.data.SynchronizedJsonReloadListener;
import tocraft.craftedcore.event.common.CommandEvents;
import tocraft.craftedcore.event.common.EntityEvents;
import tocraft.craftedcore.event.common.PlayerEvents;
import tocraft.craftedcore.event.common.ServerLevelEvents;
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

    @SubscribeEvent
    public void serverLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            ServerLevelEvents.LEVEL_LOAD.invoke().call(serverLevel);
        }
    }

    @SubscribeEvent
    public void serverUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            ServerLevelEvents.LEVEL_UNLOAD.invoke().call(serverLevel);
        }
    }
}
