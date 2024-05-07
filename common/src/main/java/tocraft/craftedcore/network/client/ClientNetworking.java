package tocraft.craftedcore.network.client;

import net.minecraft.world.entity.player.Player;
import tocraft.craftedcore.client.CraftedCoreClient;
import tocraft.craftedcore.network.ModernNetworking;

public class ClientNetworking {

    public static void runOrQueue(ModernNetworking.Context context, ApplicablePacket packet) {
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
