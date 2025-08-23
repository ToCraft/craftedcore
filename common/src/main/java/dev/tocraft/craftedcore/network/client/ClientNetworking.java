package dev.tocraft.craftedcore.network.client;

import dev.tocraft.craftedcore.client.CraftedCoreClient;
import dev.tocraft.craftedcore.network.ModernNetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class ClientNetworking {

    public static void runOrQueue(ModernNetworking.@NotNull Context context, ApplicablePacket packet) {
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
