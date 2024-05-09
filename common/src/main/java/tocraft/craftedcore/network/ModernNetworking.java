package tocraft.craftedcore.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

@SuppressWarnings("ALL")
public final class ModernNetworking {
    public static void registerReceiver(Side side, ResourceLocation id, Receiver receiver) {
        NetworkManager.registerReceiver(side == Side.C2S ? NetworkManager.Side.C2S : NetworkManager.Side.S2C, id, (packet, context) -> receiver.receive(new Context() {
            @Override
            public Player getPlayer() {
                return context.getPlayer();
            }

            @Override
            public EnvType getEnv() {
                return context.getEnv();
            }

            @Override
            public void queue(Runnable runnable) {
                context.queue(runnable);
            }
        }, packet.readNbt()));
    }

    public static void sendToPlayer(ServerPlayer player, ResourceLocation packetId, CompoundTag data) {
        FriendlyByteBuf buf = new FriendlyByteBuf((Unpooled.buffer()));
        buf.writeNbt(data);
        NetworkManager.sendToPlayer(player, packetId, buf);
    }

    public static void sendToPlayers(Iterable<ServerPlayer> players, ResourceLocation packetId, CompoundTag data) {
        for (ServerPlayer player : players) {
            FriendlyByteBuf buf = new FriendlyByteBuf((Unpooled.buffer()));
            buf.writeNbt(data);
            NetworkManager.sendToPlayer(player, packetId, buf);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void sendToServer(ResourceLocation packetId, CompoundTag data) {
        FriendlyByteBuf buf = new FriendlyByteBuf((Unpooled.buffer()));
        buf.writeNbt(data);
        NetworkManager.sendToServer(packetId, buf);
    }

    @FunctionalInterface
    public interface Receiver {
        void receive(Context context, CompoundTag data);
    }

    public interface Context {
        Player getPlayer();

        EnvType getEnv();

        void queue(Runnable runnable);
    }

    public enum Side {
        S2C, C2S
    }
}
