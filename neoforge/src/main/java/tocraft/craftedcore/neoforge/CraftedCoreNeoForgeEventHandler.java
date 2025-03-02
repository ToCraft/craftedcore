package tocraft.craftedcore.neoforge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.CanContinueSleepingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.level.SleepFinishedTimeEvent;
import tocraft.craftedcore.data.SynchronizedJsonReloadListener;
import tocraft.craftedcore.event.common.CommandEvents;
import tocraft.craftedcore.event.common.EntityEvents;
import tocraft.craftedcore.event.common.PlayerEvents;
import tocraft.craftedcore.event.common.ServerLevelEvents;
import tocraft.craftedcore.registration.SynchronizedReloadListenerRegistry;

import java.util.Map;

@SuppressWarnings("unused")
public class CraftedCoreNeoForgeEventHandler {
    @SubscribeEvent
    public void addReloadListenerEvent(AddServerReloadListenersEvent event) {
        for (Map.Entry<ResourceLocation, SynchronizedJsonReloadListener> entry : SynchronizedReloadListenerRegistry.getAllListener().entrySet()) {
            event.addListener(entry.getKey(), entry.getValue());
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
    public void allowSleepTime(CanContinueSleepingEvent event) {
        if (event.getEntity() instanceof Player player) {
            InteractionResult result = PlayerEvents.ALLOW_SLEEP_TIME.invoke().allowSleepTime(player, event.getEntity().getSleepingPos().orElse(null), event.getProblem() == null);
            if (result == InteractionResult.FAIL) {
                event.setContinueSleeping(false);
            }
            if (result == InteractionResult.SUCCESS) {
                event.setContinueSleeping(true);
            }
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

    @SubscribeEvent
    public void livingBreathe(LivingBreatheEvent event) {
        event.setCanBreathe(EntityEvents.LIVING_BREATHE.invoke().breathe(event.getEntity(), event.canBreathe()));
    }

    private void destroySpeed(PlayerEvent.BreakSpeed event) {
        PlayerEvents.DESTROY_SPEED.invoke().setDestroySpeed(event.getEntity(), event.getNewSpeed());
    }
}
