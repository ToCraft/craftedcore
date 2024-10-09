package tocraft.craftedcore.forge;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
//#if MC>1194
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
//#endif
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
//#if MC>1182
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
//#else
//$$ import net.minecraftforge.event.world.WorldEvent;
//$$ import net.minecraftforge.event.world.SleepFinishedTimeEvent;
//#endif
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
        InteractionResult result = EntityEvents.LIVING_DEATH.invoke().die((LivingEntity) event.getEntity(), event.getSource());
        if (result == InteractionResult.FAIL) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void allowSleepTime(SleepingTimeCheckEvent event) {
        InteractionResult result = PlayerEvents.ALLOW_SLEEP_TIME.invoke().allowSleepTime((Player) event.getEntity(), event.getSleepingLocation().isPresent() ? event.getSleepingLocation().get() : null, event.getResult() != Event.Result.DENY);
        if (result == InteractionResult.FAIL) {
            event.setResult(Event.Result.DENY);
        }
        if (result == InteractionResult.SUCCESS) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public void sleepFinishedTime(SleepFinishedTimeEvent event) {
        //#if MC>1182
        LevelAccessor level = event.getLevel();
        //#else
        //$$ LevelAccessor level = event.getWorld();
        //#endif
        long newTimeIn = PlayerEvents.SLEEP_FINISHED_TIME.invoke().setTimeAddition((ServerLevel) level, event.getNewTime());
        event.setTimeAddition(newTimeIn);
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        //#if MC>1182
        CommandEvents.REGISTRATION.invoke().register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
        //#else
        //$$ CommandEvents.REGISTRATION.invoke().register(event.getDispatcher(), event.getEnvironment());
        //#endif
    }

    //#if MC>1182
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
    //#else
    //$$ @SubscribeEvent
    //$$ public void serverLoad(WorldEvent.Load event) {
    //$$     if (event.getWorld() instanceof ServerLevel serverLevel) {
    //$$         ServerLevelEvents.LEVEL_LOAD.invoke().call(serverLevel);
    //$$     }
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public void serverUnload(WorldEvent.Unload event) {
    //$$     if (event.getWorld() instanceof ServerLevel serverLevel) {
    //$$         ServerLevelEvents.LEVEL_UNLOAD.invoke().call(serverLevel);
    //$$     }
    //$$ }
    //#endif

    //#if MC>1194
    @SubscribeEvent
    public void livingBreathe(LivingBreatheEvent event) {
        event.setCanBreathe(EntityEvents.LIVING_BREATHE.invoke().breathe(event.getEntity(), event.canBreathe()));
    }
    //#endif

    private void destroySpeed(PlayerEvent.BreakSpeed event) {
        PlayerEvents.DESTROY_SPEED.invoke().setDestroySpeed((Player) event.getEntity(), event.getNewSpeed());
    }
}
