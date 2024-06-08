package tocraft.craftedcore.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.util.TraceUtils;

// based on code by comp500
@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(method = "crash", at = @At("HEAD"))
    private static void onCrash(CrashReport report, CallbackInfo ci) {
        StringBuilder crashReportBuilder = new StringBuilder();
        TraceUtils.printMixinTrace(report.getException().getStackTrace(), crashReportBuilder);
        System.out.println(crashReportBuilder);
    }
}
