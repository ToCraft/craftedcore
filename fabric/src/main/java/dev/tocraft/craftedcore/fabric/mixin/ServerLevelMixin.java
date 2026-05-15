package dev.tocraft.craftedcore.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.tocraft.craftedcore.event.common.PlayerEvents;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.clock.ClockTimeMarker;
import net.minecraft.world.clock.ServerClockManager;
import net.minecraft.world.clock.WorldClock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/clock/ServerClockManager;moveToTimeMarker(Lnet/minecraft/core/Holder;Lnet/minecraft/resources/ResourceKey;)Z"))
    private boolean fixStartSleep(ServerClockManager instance, Holder<@NotNull WorldClock> clock, ResourceKey<@NotNull ClockTimeMarker> timeMarkerId, Operation<Boolean> original) {
        ServerLevel level = (ServerLevel) (Object) this;
        Optional<Long> result = PlayerEvents.SLEEP_FINISHED_TIME.invoke().setWakeUpTime(level, level.getDefaultClockTime(), -1);
        if (result.isPresent()) {
            level.clockManager().setTotalTicks(clock, result.get());
            return true;
        }
        return original.call(instance, clock, timeMarkerId);
    }
}
