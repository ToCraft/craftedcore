package tocraft.craftedcore.network;

import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;

public interface PacketSink {
    static PacketSink ofPlayer(ServerPlayer player) {
        return packet -> Objects.requireNonNull(player, "Unable to send packet to a 'null' player!").connection.send(packet);
    }
    
    static PacketSink ofPlayers(Iterable<? extends ServerPlayer> players) {
        return packet -> {
            for (var player : players) {
                Objects.requireNonNull(player, "Unable to send packet to a 'null' player!").connection.send(packet);
            }
        };
    }
    
    static PacketSink client() {
        return packet -> {
            if (Minecraft.getInstance().getConnection() != null) {
                Minecraft.getInstance().getConnection().send(packet);
            } else {
                throw new IllegalStateException("Unable to send packet to the server while not in game!");
            }
        };
    }
    
    void accept(Packet<?> packet);
}