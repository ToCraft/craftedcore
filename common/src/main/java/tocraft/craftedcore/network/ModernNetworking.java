package tocraft.craftedcore.network;

import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import tocraft.craftedcore.platform.PlatformData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public final class ModernNetworking {
    private static final Map<ResourceLocation, CustomPacketPayload.Type<PacketPayload>> TYPES = new HashMap<>();

    public static void registerReceiver(Side side, ResourceLocation id, Receiver receiver) {
        TYPES.put(id, new CustomPacketPayload.Type<>(id));
        NetworkManager.registerReceiver(side == Side.C2S ? NetworkManager.Side.C2S : NetworkManager.Side.S2C, TYPES.get(id), CustomPacketPayload.codec(PacketPayload::write, PacketPayload::new), (packet, context) -> receiver.receive(new Context() {
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
        }, packet.nbt()));
    }

    public static void registerType(ResourceLocation id) {
        if (PlatformData.getEnv() == EnvType.SERVER) {
            if (!TYPES.containsKey(id)) {
                TYPES.put(id, new CustomPacketPayload.Type<>(id));
            }
            NetworkManager.registerS2CPayloadType(TYPES.get(id), PacketPayload.streamCodec(TYPES.get(id)), List.of());
        }
    }

    public static void sendToPlayer(ServerPlayer player, ResourceLocation packetId, CompoundTag data) {
        NetworkManager.sendToPlayer(player, new PacketPayload(packetId, data));
    }

    public static void sendToPlayers(Iterable<ServerPlayer> players, ResourceLocation packetId, CompoundTag data) {
        for (ServerPlayer player : players) {
            NetworkManager.sendToPlayer(player, new PacketPayload(packetId, data));
        }
    }

    @Environment(EnvType.CLIENT)
    public static void sendToServer(ResourceLocation packetId, CompoundTag data) {
        NetworkManager.sendToServer(new PacketPayload(packetId, data));
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

    private record PacketPayload(ResourceLocation id,
                                 CompoundTag nbt) implements CustomPacketPayload {

        public PacketPayload(RegistryFriendlyByteBuf buf) {
            this(buf.readResourceLocation(), buf.readNbt());
        }

        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeResourceLocation(id());
            buf.writeNbt(nbt);
        }

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPES.get(id);
        }

        public static StreamCodec<RegistryFriendlyByteBuf, PacketPayload> streamCodec(Type<PacketPayload> type) {
            return new StreamCodec<RegistryFriendlyByteBuf, PacketPayload>() {
                @Override
                public PacketPayload decode(RegistryFriendlyByteBuf buf) {
                    return new PacketPayload(buf);
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, PacketPayload payload) {
                    buf.writeResourceLocation(payload.id());
                    buf.writeNbt(payload.nbt());
                }
            };
        }
    }
}
