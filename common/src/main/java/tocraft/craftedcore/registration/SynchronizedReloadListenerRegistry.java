package tocraft.craftedcore.registration;

import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import tocraft.craftedcore.data.SynchronizedJsonReloadListener;
import tocraft.craftedcore.network.ModernNetworking;
import tocraft.craftedcore.platform.PlatformData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SynchronizedReloadListenerRegistry {
    private static final Map<ResourceLocation, SynchronizedJsonReloadListener> listener = new HashMap<>();

    /**
     * Register a serverside synchronized json reload listener
     */
    @SuppressWarnings("unused")
    public static void register(SynchronizedJsonReloadListener reloadListener, ResourceLocation id) {
        listener.put(id, reloadListener);

        // Register Data Packet receiver
        if (PlatformData.getEnv() == EnvType.CLIENT) {
            reloadListener.registerPacketReceiver();
        }


        // register Network packet
        ModernNetworking.registerType(reloadListener.RELOAD_SYNC);
    }

    public static Map<ResourceLocation, SynchronizedJsonReloadListener> getAllListener() {
        return new HashMap<>(listener);
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
