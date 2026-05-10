package dev.tocraft.craftedcore.registration;

import dev.tocraft.craftedcore.data.SynchronizedJsonReloadListener;
import dev.tocraft.craftedcore.event.common.ResourceEvents;
import dev.tocraft.craftedcore.network.ModernNetworking;
import dev.tocraft.craftedcore.platform.PlatformData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

@SuppressWarnings("UnreachableCode")
public class SynchronizedReloadListenerRegistry {
    private static final SynchronizedReloadListenerRegistryService SERVICE =
            ServiceLoader.load(SynchronizedReloadListenerRegistryService.class).findFirst().orElseThrow();
    private static final Map<Identifier, SynchronizedJsonReloadListener> listener = new HashMap<>();

    @SuppressWarnings("unused")
    public static void register(SynchronizedJsonReloadListener reloadListener, Identifier id) {
        listener.put(id, reloadListener);

        if (PlatformData.getEnv() == PlatformData.Env.CLIENT) {
            reloadListener.registerPacketReceiver();
        }

        ModernNetworking.registerType(reloadListener.RELOAD_SYNC);

        SERVICE.onRegister(reloadListener, id);
    }

    @Contract(" -> new")
    @ApiStatus.Internal
    public static @NotNull Map<Identifier, SynchronizedJsonReloadListener> getAllListener() {
        return new HashMap<>(listener);
    }

    @SuppressWarnings("unused")
    public static SynchronizedJsonReloadListener get(Identifier id) {
        return listener.get(id);
    }

    public static void initialize() {
        ResourceEvents.DATA_PACK_SYNC.register(SynchronizedReloadListenerRegistry::sendAllToPlayer);
    }

    private static void sendAllToPlayer(ServerPlayer player) {
        for (SynchronizedJsonReloadListener reloadListener : listener.values()) {
            reloadListener.sendSyncPacket(player);
        }
    }
}
