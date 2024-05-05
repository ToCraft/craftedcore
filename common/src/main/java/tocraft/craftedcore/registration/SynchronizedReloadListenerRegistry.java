package tocraft.craftedcore.registration;

import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import tocraft.craftedcore.data.SynchronizedJsonReloadListener;

import java.util.HashMap;
import java.util.Map;

public class SynchronizedReloadListenerRegistry {
    private static final Map<ResourceLocation, SynchronizedJsonReloadListener> listener = new HashMap<>();

    /**
     * Register a serverside synchronized json reload listener
     */
    @SuppressWarnings("unused")
    public static void register(SynchronizedJsonReloadListener reloadListener, ResourceLocation id) {

        ReloadListenerRegistry.register(PackType.SERVER_DATA, reloadListener, id);
        listener.put(id, reloadListener);

        // Register Data Packet receiver
        if (Platform.getEnv() == EnvType.CLIENT) {
            reloadListener.registerPacketReceiver();
        }
    }

    @SuppressWarnings("unused")
    public static SynchronizedJsonReloadListener get(ResourceLocation id) {
        return listener.get(id);
    }

    public static void sendAllToPlayer(ServerPlayer player) {
        for (SynchronizedJsonReloadListener reloadListener : listener.values()) {
            reloadListener.sendSyncPacket(player);
        }
    }
}
