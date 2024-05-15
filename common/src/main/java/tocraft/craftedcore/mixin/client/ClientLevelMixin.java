package tocraft.craftedcore.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.event.client.ClientTickEvents;

import java.util.function.Supplier;

@SuppressWarnings({"unused", "DataFlowIssue"})
@Environment(EnvType.CLIENT)
@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {

    protected ClientLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }

    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void tickEntities(CallbackInfo ci) {
        ProfilerFiller profiler = getProfiler();
        profiler.push("CraftedCoreClientLevelPreTick");
        ClientTickEvents.CLIENT_LEVEL_PRE.invoke().tick((ClientLevel) (Object) this);
        profiler.pop();
    }

    @Inject(method = "tickEntities", at = @At("RETURN"))
    private void tickEntitiesPost(CallbackInfo ci) {
        ProfilerFiller profiler = getProfiler();
        profiler.push("CraftedCoreClientLevelPostTick");
        ClientTickEvents.CLIENT_LEVEL_POST.invoke().tick((ClientLevel) (Object) this);
        profiler.pop();
    }
}
