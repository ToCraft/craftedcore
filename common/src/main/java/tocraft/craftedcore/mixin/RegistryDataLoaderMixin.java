package tocraft.craftedcore.mixin;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryDataLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin {
    @Shadow
    @Final
    @Mutable
    public static List<RegistryDataLoader.RegistryData<?>> WORLDGEN_REGISTRIES;
    @Shadow
    @Final
    @Mutable
    public static List<RegistryDataLoader.RegistryData<?>> DIMENSION_REGISTRIES;
    @Shadow
    @Final
    @Mutable
    public static List<RegistryDataLoader.RegistryData<?>> SYNCHRONIZED_REGISTRIES;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void onInit(CallbackInfo ci) {
        // make lists mutable from everywhere (a bit unsafe...)
        WORLDGEN_REGISTRIES = new ArrayList<>(WORLDGEN_REGISTRIES);
        DIMENSION_REGISTRIES = new ArrayList<>(DIMENSION_REGISTRIES);
        SYNCHRONIZED_REGISTRIES = new ArrayList<>(SYNCHRONIZED_REGISTRIES);
    }
}
