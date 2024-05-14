package tocraft.craftedcore.network;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ModernNetworking {
    @ExpectPlatform
    public static void registerReceiver(Side side, ResourceLocation id, Receiver receiver) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerType(ResourceLocation id) {
        throw new AssertionError();
    }

    public static void sendToPlayer(ServerPlayer player, ResourceLocation packetId, CompoundTag data) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeResourceLocation(packetId);
        buf.writeNbt(data.copy());
        player.connection.send(new ClientboundCustomPayloadPacket(buf));
    }

    public static void sendToPlayers(Iterable<ServerPlayer> players, ResourceLocation packetId, CompoundTag data) {
        for (ServerPlayer player : players) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeResourceLocation(packetId);
            buf.writeNbt(data.copy());
            player.connection.send(new ClientboundCustomPayloadPacket(buf));
        }
    }

    public static void sendToServer(ResourceLocation packetId, CompoundTag data) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();

        if (connection != null) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeResourceLocation(packetId);
            buf.writeNbt(data.copy());
            connection.send(new ServerboundCustomPayloadPacket(buf));
        }
    }

    @FunctionalInterface
    public interface Receiver {
        void receive(Context context, CompoundTag data);
    }

    public interface Context {
        Player getPlayer();

        Env getEnv();

        void queue(Runnable runnable);
    }

    public enum Side {
        S2C, C2S
    }

    public enum Env {
        CLIENT, SERVER
    }

    @ApiStatus.Internal
    public record PacketPayload(ResourceLocation id,
                                CompoundTag nbt) implements CustomPacketPayload {

        public PacketPayload(FriendlyByteBuf buf) {
            this(buf.readResourceLocation(), buf.readNbt());
        }

        public void write(FriendlyByteBuf buf) {
            buf.writeResourceLocation(id);
            buf.writeNbt(nbt);
        }
    }
}
