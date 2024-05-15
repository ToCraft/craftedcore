package tocraft.craftedcore.registration.fabric;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import tocraft.craftedcore.data.SynchronizedJsonReloadListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@SuppressWarnings("unused")
public class SynchronizedReloadListenerRegistryImpl {
    public static void onRegister(SynchronizedJsonReloadListener reloadListener, ResourceLocation id) {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return id;
            }

            @Override
            public @NotNull String getName() {
                return reloadListener.getName();
            }

            @Override
            public @NotNull CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                return reloadListener.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
            }
        });
    }
}
