package tocraft.craftedcore.client;

import dev.architectury.event.events.client.ClientPlayerEvent;
import tocraft.craftedcore.config.ConfigLoader;
import tocraft.craftedcore.data.PlayerDataSynchronizer;
import tocraft.craftedcore.event.ArchitecturyImpl;
import tocraft.craftedcore.event.client.ClientPlayerEvents;
import tocraft.craftedcore.network.client.ClientNetworking.ApplicablePacket;

import java.util.HashSet;
import java.util.Set;

public class CraftedCoreClient {
    private static final Set<ApplicablePacket> SYNC_PACKET_QUEUE = new HashSet<>();

    public void initialize() {
        ArchitecturyImpl.clientInitialize();

        ConfigLoader.registerConfigSyncHandler();
        PlayerDataSynchronizer.registerPacketHandler();

        ClientPlayerEvents.CLIENT_PLAYER_JOIN.register(player -> {
            for (ApplicablePacket packet : getSyncPacketQueue()) {
                packet.apply(player);
            }

            getSyncPacketQueue().clear();
        });
    }

    public static Set<ApplicablePacket> getSyncPacketQueue() {
        return SYNC_PACKET_QUEUE;
    }
}
