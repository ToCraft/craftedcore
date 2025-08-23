package dev.tocraft.craftedcore.mixin;

import dev.tocraft.craftedcore.util.TraceUtils;
import net.minecraft.CrashReportCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// based on code by comp500 (licensed as MIT, take a look https://github.com/comp500/mixintrace for details)
@SuppressWarnings("unused")
@Mixin(CrashReportCategory.class)
public abstract class CrashReportCategoryMixin {
    @Shadow
    private StackTraceElement[] stackTrace;

    @Inject(method = "getDetails", at = @At("TAIL"))
    private void onGetDetails(StringBuilder crashReportBuilder, CallbackInfo ci) {
        TraceUtils.printMixinTrace(stackTrace, crashReportBuilder);
    }
}
