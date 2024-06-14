package tocraft.craftedcore.mixin;

import net.minecraft.CrashReportCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.platform.PlatformData;
import tocraft.craftedcore.util.TraceUtils;

// based on code by comp500 (licensed as MIT, take a look https://github.com/comp500/mixintrace for details)
@Mixin(CrashReportCategory.class)
public abstract class CrashReportCategoryMixin {
    @Shadow
    private StackTraceElement[] stackTrace;

    @Inject(method = "getDetails", at = @At("TAIL"))
    private void onGetDetails(StringBuilder crashReportBuilder, CallbackInfo ci) {
        if (!PlatformData.isModLoaded("mixintrace")) {
            TraceUtils.printMixinTrace(stackTrace, crashReportBuilder);
        }
    }
}
