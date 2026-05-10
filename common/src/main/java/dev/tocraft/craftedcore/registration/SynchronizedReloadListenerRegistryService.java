package dev.tocraft.craftedcore.registration;

import dev.tocraft.craftedcore.data.SynchronizedJsonReloadListener;
import net.minecraft.resources.Identifier;

public interface SynchronizedReloadListenerRegistryService {
    void onRegister(SynchronizedJsonReloadListener reloadListener, Identifier id);
}
