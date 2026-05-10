package dev.tocraft.craftedcore.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

@SuppressWarnings("unused")
public class ModernNetworking {
    private static final ModernNetworkingService SERVICE =
            ServiceLoader.load(ModernNetworkingService.class).findFirst().orElseThrow();
    private static final Map<Identifier, CustomPacketPayload.Type<PacketPayload>> TYPES = new HashMap<>();

    public static void registerReceiver(Side side, Identifier id, Receiver receiver) {
        SERVICE.registerReceiver(side, id, receiver);
    }

    public static void registerType(Identifier id) {
        SERVICE.registerType(id);
    }

    public static CustomPacketPayload.Type<PacketPayload> getType(Identifier id) {
        if (!TYPES.containsKey(id)) {
            TYPES.put(id, new CustomPacketPayload.Type<>(id));
        }
        return TYPES.get(id);
    }

    public static void sendToPlayer(@NotNull ServerPlayer player, Identifier packetId, CompoundTag data) {
        player.connection.send(toPacket(Side.S2C, new PacketPayload(packetId, data)));
    }

    public static void sendToPlayers(@NotNull Iterable<ServerPlayer> players, Identifier packetId, CompoundTag data) {
        for (ServerPlayer player : players) {
            sendToPlayer(player, packetId, data);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void sendToServer(Identifier packetId, CompoundTag data) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();

        if (connection != null) {
            connection.send(toPacket(Side.C2S, new PacketPayload(packetId, data)));
        }
    }

    @ApiStatus.Internal
    public static Packet<?> toPacket(ModernNetworking.Side side, CustomPacketPayload payload) {
        return SERVICE.toPacket(side, payload);
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
    public record PacketPayload(Identifier id,
                                CompoundTag nbt) implements CustomPacketPayload {
        public void write(@NotNull RegistryFriendlyByteBuf buf) {
            buf.writeIdentifier(id);
            buf.writeNbt(nbt);
        }


        public PacketPayload(@NotNull RegistryFriendlyByteBuf buf) {
            this(buf.readIdentifier(), buf.readNbt());
        }

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return getType(id);
        }

        @Contract(value = " -> new", pure = true)
        public static @NotNull StreamCodec<RegistryFriendlyByteBuf, PacketPayload> streamCodec() {
            return new StreamCodec<>() {
                @Override
                public @NotNull PacketPayload decode(@NotNull RegistryFriendlyByteBuf buf) {
                    return new PacketPayload(buf);
                }

                @Override
                public void encode(@NotNull RegistryFriendlyByteBuf buf, @NotNull PacketPayload payload) {
                    buf.writeIdentifier(payload.id);
                    buf.writeNbt(payload.nbt);
                }
            };
        }
    }
}
