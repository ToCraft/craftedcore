package tocraft.craftedcore.network;

import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public final class ModernNetworking {
    private static final Map<ResourceLocation, CustomPacketPayload.Type<PacketPayload>> TYPES = new HashMap<>();

    public static void registerReceiver(NetworkManager.Side side, ResourceLocation id, Receiver receiver) {
        TYPES.put(id, new CustomPacketPayload.Type<>(id));
        NetworkManager.registerReceiver(side, TYPES.get(id), CustomPacketPayload.codec(PacketPayload::write, PacketPayload::new), (packet, context) -> receiver.receive(packet.nbt(), context));
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
        void receive(CompoundTag data, NetworkManager.PacketContext context);
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
            return TYPES.get(id());
        }
    }
}
