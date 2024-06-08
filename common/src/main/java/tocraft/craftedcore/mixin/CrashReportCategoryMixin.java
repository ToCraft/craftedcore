package tocraft.craftedcore.mixin;

import net.minecraft.CrashReportCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.util.TraceUtils;

@Mixin(CrashReportCategory.class)
public abstract class CrashReportCategoryMixin {
    @Shadow
    private StackTraceElement[] stackTrace;

    @Inject(method = "getDetails", at = @At("TAIL"))
    private void onGetDetails(StringBuilder crashReportBuilder, CallbackInfo ci) {
        TraceUtils.printMixinTrace(stackTrace, crashReportBuilder);
    }
}
