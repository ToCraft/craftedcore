package tocraft.craftedcore.fabric;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import tocraft.craftedcore.data.SynchronizedJsonReloadListener;
import tocraft.craftedcore.event.common.CommandEvents;
import tocraft.craftedcore.event.common.PlayerEvents;
import tocraft.craftedcore.registration.SynchronizedReloadListenerRegistry;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class CraftedCoreFabricEventHandler {

    public static void initialize() {
        EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, sleepingPos, vanillaResult) -> PlayerEvents.ALLOW_SLEEP_TIME.invoke().allowSleepTime(player, sleepingPos, vanillaResult));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CommandEvents.REGISTRATION.invoke().register(dispatcher, registryAccess, environment));
        for (Map.Entry<ResourceLocation, SynchronizedJsonReloadListener> listener : SynchronizedReloadListenerRegistry.getAllListener().entrySet()) {
            ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
                @Override
                public ResourceLocation getFabricId() {
                    return listener.getKey();
                }

                @Override
                public @NotNull CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                    return listener.getValue().reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
                }
            });
        }
    }
}
