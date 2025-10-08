package dev.tocraft.craftedcore.registration.fabric;

import dev.tocraft.craftedcore.data.SynchronizedJsonReloadListener;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings("unused")
@ApiStatus.Internal
public class SynchronizedReloadListenerRegistryImpl {
    public static void onRegister(SynchronizedJsonReloadListener reloadListener, ResourceLocation id) {
        ResourceLoader.get(PackType.SERVER_DATA).registerReloader(id, reloadListener);
    }
}
