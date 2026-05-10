package dev.tocraft.craftedcore.registration.neoforge;

import dev.tocraft.craftedcore.data.SynchronizedJsonReloadListener;
import dev.tocraft.craftedcore.registration.SynchronizedReloadListenerRegistryService;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings("unused")
@ApiStatus.Internal
public class SynchronizedReloadListenerRegistryImpl implements SynchronizedReloadListenerRegistryService {
    @Override
    @SuppressWarnings("EmptyMethod")
    public void onRegister(SynchronizedJsonReloadListener reloadListener, Identifier id) {
    }
}
