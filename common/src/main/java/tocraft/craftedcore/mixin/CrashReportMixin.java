package tocraft.craftedcore.mixin;

import net.minecraft.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.util.TraceUtils;

@Mixin(CrashReport.class)
public abstract class CrashReportMixin {
    @Shadow
    private StackTraceElement[] uncategorizedStackTrace;

    @Inject(method = "getDetails(Ljava/lang/StringBuilder;)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/CrashReport;details:Ljava/util/List;"))
    private void onGetDetails(StringBuilder crashReportBuilder, CallbackInfo ci) {
        int trailingNewlineCount = 0;
        // Remove trailing \n
        if (crashReportBuilder.charAt(crashReportBuilder.length() - 1) == '\n') {
            crashReportBuilder.deleteCharAt(crashReportBuilder.length() - 1);
            trailingNewlineCount++;
        }
        if (crashReportBuilder.charAt(crashReportBuilder.length() - 1) == '\n') {
            crashReportBuilder.deleteCharAt(crashReportBuilder.length() - 1);
            trailingNewlineCount++;
        }
        TraceUtils.printMixinTrace(uncategorizedStackTrace, crashReportBuilder);
        crashReportBuilder.append("\n".repeat(trailingNewlineCount));
    }
}