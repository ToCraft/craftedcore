package tocraft.craftedcore.network.client;

import net.minecraft.world.entity.player.Player;
import tocraft.craftedcore.client.CraftedCoreClient;
import tocraft.craftedcore.network.NetworkManager;

public class ClientNetworking {
	
	public static void runOrQueue(NetworkManager.PacketContext context, ApplicablePacket packet) {
		if (context.getPlayer() == null) {
			CraftedCoreClient.getSyncPacketQueue().add(packet);
		} else {
			context.queue(() -> packet.apply(context.getPlayer()));
		}
	}
	
	@FunctionalInterface
	public interface ApplicablePacket {
	    void apply(Player player);
	}
}
