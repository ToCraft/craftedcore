package tocraft.craftedcore.mixin;

import net.minecraft.CrashReport;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.util.TraceUtils;

// based on code by comp500
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(method = "onServerCrash", at = @At("HEAD"))
    private void onOnServerCrash(@NotNull CrashReport report, CallbackInfo ci) {
        StringBuilder crashReportBuilder = new StringBuilder();
        TraceUtils.printMixinTrace(report.getException().getStackTrace(), crashReportBuilder);
        System.out.println(crashReportBuilder);
    }
}

