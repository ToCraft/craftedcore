package tocraft.craftedcore.network;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
//#if MC>=1205
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
//#endif
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
//#if MC>1201
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//#endif
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class ModernNetworking {
    //#if MC>=1205
    private static final Map<ResourceLocation, CustomPacketPayload.Type<PacketPayload>> TYPES = new HashMap<>();
    //#endif

    @ExpectPlatform
    public static void registerReceiver(Side side, ResourceLocation id, Receiver receiver) {
        throw new AssertionError();
    }

    //#if MC>=1205
    @ExpectPlatform
    public static void registerType(ResourceLocation id) {
        throw new AssertionError();
    }
    
    public static CustomPacketPayload.Type<PacketPayload> getType(ResourceLocation id) {
        if (!TYPES.containsKey(id)) {
            TYPES.put(id, new CustomPacketPayload.Type<>(id));
        }
        return TYPES.get(id);
    }
    //#endif

    public static void sendToPlayer(ServerPlayer player, ResourceLocation packetId, CompoundTag data) {
        //#if MC>1201
        player.connection.send(toPacket(Side.S2C, new PacketPayload(packetId, data.copy())));
        //#else
        //$$ FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        //$$ buf.writeResourceLocation(packetId);
        //$$ buf.writeNbt(data.copy());
        //$$ player.connection.send(toPacket(Side.S2C, packetId, buf));
        //#endif
    }

    public static void sendToPlayers(Iterable<ServerPlayer> players, ResourceLocation packetId, CompoundTag data) {
        for (ServerPlayer player : players) {
            sendToPlayer(player, packetId, data);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void sendToServer(ResourceLocation packetId, CompoundTag data) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();

        if (connection != null) {
            //#if MC>1201
            connection.send(toPacket(Side.C2S, new PacketPayload(packetId, data)));
            //#else
            //$$ FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            //$$ buf.writeResourceLocation(packetId);
            //$$ buf.writeNbt(data.copy());
            //$$ connection.send(toPacket(Side.C2S, packetId, buf));
            //#endif
        }
    }

    @ExpectPlatform
    @ApiStatus.Internal
    //#if MC>1201
    public static Packet<?> toPacket(ModernNetworking.Side side, CustomPacketPayload payload) {
    //#else
    //$$ public static Packet<?> toPacket(ModernNetworking.Side side, ResourceLocation id, FriendlyByteBuf buf) {
    //#endif
        throw new AssertionError();
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

    //#if MC>1201
    @ApiStatus.Internal
    public record PacketPayload(ResourceLocation id,
                                CompoundTag nbt) implements CustomPacketPayload {
        //#if MC>=1205
        public void write(RegistryFriendlyByteBuf buf) {
        //#else
        //$$ public PacketPayload(FriendlyByteBuf buf) {
        //$$     this(buf.readResourceLocation(), buf.readNbt());
        //$$ }
        //$$
        //$$ public void write(FriendlyByteBuf buf) {
        //#endif
            buf.writeResourceLocation(id);
            buf.writeNbt(nbt);
        }
    
        //#if MC>=1205
        
        public PacketPayload(RegistryFriendlyByteBuf buf) {
            this(buf.readResourceLocation(), buf.readNbt());
        }
        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return getType(id);
        }
        
        public static StreamCodec<RegistryFriendlyByteBuf, PacketPayload> streamCodec() {
            return new StreamCodec<>() {
                @Override
                public @NotNull PacketPayload decode(@NotNull RegistryFriendlyByteBuf buf) {
                    return new PacketPayload(buf);
                }
        
                @Override
                public void encode(@NotNull RegistryFriendlyByteBuf buf, @NotNull PacketPayload payload) {
                    buf.writeResourceLocation(payload.id);
                    buf.writeNbt(payload.nbt);
                }
            };
        }
        //#endif
    }
    //#endif
}
