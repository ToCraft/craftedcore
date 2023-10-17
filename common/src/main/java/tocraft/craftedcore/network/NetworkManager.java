package tocraft.craftedcore.network;

import java.util.Collections;
import java.util.List;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class NetworkManager {
    public static void registerReceiver(Side side, ResourceLocation id, NetworkReceiver receiver) {
        registerReceiver(side, id, Collections.emptyList(), receiver);
    }
    
    @ExpectPlatform
    public static void registerReceiver(Side side, ResourceLocation id, List<PacketTransformer> packetTransformers, NetworkReceiver receiver) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static void collectPackets(PacketSink sink, Side side, ResourceLocation id, FriendlyByteBuf buf) {
        throw new AssertionError();
    }
    
    public static void sendToPlayer(ServerPlayer player, ResourceLocation id, FriendlyByteBuf buf) {
        collectPackets(PacketSink.ofPlayer(player), serverToClient(), id, buf);
    }
    
    public static void sendToPlayers(Iterable<ServerPlayer> players, ResourceLocation id, FriendlyByteBuf buf) {
        collectPackets(PacketSink.ofPlayers(players), serverToClient(), id, buf);
    }
    
    @Environment(EnvType.CLIENT)
    public static void sendToServer(ResourceLocation id, FriendlyByteBuf buf) {
        collectPackets(PacketSink.client(), clientToServer(), id, buf);
    }
    
    @Environment(EnvType.CLIENT)
    @ExpectPlatform
    public static boolean canServerReceive(ResourceLocation id) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static boolean canPlayerReceive(ServerPlayer player, ResourceLocation id) {
        throw new AssertionError();
    }
    
    @FunctionalInterface
    public interface NetworkReceiver {
        void receive(FriendlyByteBuf buf, PacketContext context);
    }
    
    public interface PacketContext {
        Player getPlayer();
        
        void queue(Runnable runnable);
    }
    
    public static Side s2c() {
        return Side.S2C;
    }
    
    public static Side c2s() {
        return Side.C2S;
    }
    
    public static Side serverToClient() {
        return Side.S2C;
    }
    
    public static Side clientToServer() {
        return Side.C2S;
    }
    
    public enum Side {
        S2C,
        C2S
    }
}
