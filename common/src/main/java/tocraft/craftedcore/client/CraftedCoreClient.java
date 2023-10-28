package tocraft.craftedcore.client;

import java.util.HashSet;
import java.util.Set;

import tocraft.craftedcore.config.ConfigLoader;
import tocraft.craftedcore.data.PlayerDataSynchronizer;
import tocraft.craftedcore.events.client.ClientPlayerEvents;
import tocraft.craftedcore.network.client.ClientNetworking.ApplicablePacket;

public class CraftedCoreClient {
	private static final Set<ApplicablePacket> SYNC_PACKET_QUEUE = new HashSet<>();

	public void initialize() {
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
