package tocraft.craftedcore.registration;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.data.SynchronizedJsonReloadListener;
import tocraft.craftedcore.event.common.ResourceEvents;
import tocraft.craftedcore.platform.PlatformData;

//#if MC>=1205
import tocraft.craftedcore.network.ModernNetworking;
//#endif

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnreachableCode")
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


        //#if MC>=1205
        // register Network packet
        ModernNetworking.registerType(reloadListener.RELOAD_SYNC);
        //#endif

        onRegister(reloadListener, id);
    }

    @SuppressWarnings("unused")
    @ApiStatus.Internal
    @ExpectPlatform
    private static void onRegister(SynchronizedJsonReloadListener reloadListener, ResourceLocation id) {
        throw new AssertionError();
    }

    @Contract(" -> new")
    @ApiStatus.Internal
    public static @NotNull List<SynchronizedJsonReloadListener> getAllListener() {
        return new ArrayList<>(listener.values());
    }

    @SuppressWarnings("unused")
    public static SynchronizedJsonReloadListener get(ResourceLocation id) {
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
