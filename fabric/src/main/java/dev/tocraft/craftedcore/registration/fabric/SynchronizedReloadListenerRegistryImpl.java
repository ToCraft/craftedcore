package dev.tocraft.craftedcore.registration.fabric;

import dev.tocraft.craftedcore.data.SynchronizedJsonReloadListener;
import dev.tocraft.craftedcore.registration.SynchronizedReloadListenerRegistryService;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings("unused")
@ApiStatus.Internal
public class SynchronizedReloadListenerRegistryImpl implements SynchronizedReloadListenerRegistryService {
    @Override
    public void onRegister(SynchronizedJsonReloadListener reloadListener, Identifier id) {
        ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(id, reloadListener);
    }
}
