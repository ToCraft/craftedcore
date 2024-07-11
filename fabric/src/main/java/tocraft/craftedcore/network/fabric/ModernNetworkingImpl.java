package tocraft.craftedcore.network.fabric;

//#if MC>=1205
//$$ import net.fabricmc.api.EnvType;
//$$ import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
//$$ import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
//$$ import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
//$$ import net.fabricmc.loader.api.FabricLoader;
//$$ import net.minecraft.nbt.CompoundTag;
//$$ import net.minecraft.network.FriendlyByteBuf;
//$$ import net.minecraft.network.protocol.Packet;
//$$ import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
//$$ import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
//$$ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//$$ import net.minecraft.resources.ResourceLocation;
//$$ import net.minecraft.world.entity.player.Player;
//$$ import org.jetbrains.annotations.ApiStatus;
//$$ import tocraft.craftedcore.network.ModernNetworking;
//$$ import tocraft.craftedcore.network.ModernNetworking.PacketPayload;
//$$
//$$ import static tocraft.craftedcore.network.ModernNetworking.getType;
//$$
//$$ @SuppressWarnings({"unused", "resource"})
//$$ public class ModernNetworkingImpl {
//$$     public static void registerReceiver(ModernNetworking.Side side, ResourceLocation id, ModernNetworking.Receiver receiver) {
//$$         if (side == ModernNetworking.Side.C2S) {
//$$             PayloadTypeRegistry.playC2S().register(getType(id), PacketPayload.streamCodec());
//$$             ServerPlayNetworking.registerGlobalReceiver(getType(id), (payload, context) -> receiver.receive(new ModernNetworking.Context() {
//$$                 @Override
//$$                 public Player getPlayer() {
//$$                     return context.player();
//$$                 }
//$$
//$$                 @Override
//$$                 public ModernNetworking.Env getEnv() {
//$$                     return ModernNetworking.Env.SERVER;
//$$                 }
//$$
//$$                 @Override
//$$                 public void queue(Runnable runnable) {
//$$                     context.player().server.execute(runnable);
//$$                 }
//$$             }, payload.nbt()));
//$$         } else if (side == ModernNetworking.Side.S2C) {
//$$             PayloadTypeRegistry.playS2C().register(getType(id), PacketPayload.streamCodec());
//$$             ClientPlayNetworking.registerGlobalReceiver(getType(id), (payload, context) -> receiver.receive(new ModernNetworking.Context() {
//$$                 @Override
//$$                 public Player getPlayer() {
//$$                     return context.player();
//$$                 }
//$$
//$$                 @Override
//$$                 public ModernNetworking.Env getEnv() {
//$$                     return ModernNetworking.Env.CLIENT;
//$$                 }
//$$
//$$                 @Override
//$$                 public void queue(Runnable runnable) {
//$$                     context.client().execute(runnable);
//$$                 }
//$$             }, payload.nbt()));
//$$         }
//$$     }
//$$
//$$     public static void registerType(ResourceLocation id) {
//$$         if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
//$$             ModernNetworking.getType(id);
//$$             PayloadTypeRegistry.playS2C().register(getType(id), PacketPayload.streamCodec());
//$$         }
//$$     }
//$$
//$$     @ApiStatus.Internal
//$$     public static Packet<?> toPacket(ModernNetworking.Side side, CustomPacketPayload payload) {
//$$         if (side == ModernNetworking.Side.C2S) {
//$$             return new ServerboundCustomPayloadPacket(payload);
//$$         } else {
//$$             return new ClientboundCustomPayloadPacket(payload);
//$$         }
//$$     }
//$$ }
//#else

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
//#if MC>1201
//$$ import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
//$$ import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
//#else
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.network.FriendlyByteBuf;
//#endif
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import tocraft.craftedcore.network.ModernNetworking;

@SuppressWarnings({"unused", "resource"})
public class ModernNetworkingImpl {
    public static void registerReceiver(ModernNetworking.Side side, ResourceLocation id, ModernNetworking.Receiver receiver) {
        if (side == ModernNetworking.Side.C2S) {
            ServerPlayNetworking.registerGlobalReceiver(id, (server, player, handler, buf, responseSender) -> {
                CompoundTag data = buf.readNbt();
                receiver.receive(new ModernNetworking.Context() {
                    @Override
                    public Player getPlayer() {
                        return player;
                    }

                    @Override
                    public ModernNetworking.Env getEnv() {
                        return ModernNetworking.Env.SERVER;
                    }

                    @Override
                    public void queue(Runnable runnable) {
                        server.execute(runnable);
                    }
                }, data);
            });
        } else if (side == ModernNetworking.Side.S2C) {
            ClientPlayNetworking.registerGlobalReceiver(id, (client, handler, buf, responseSender) -> {
                CompoundTag data = buf.readNbt();
                receiver.receive(new ModernNetworking.Context() {
                    @Override
                    public Player getPlayer() {
                        return client.player;
                    }

                    @Override
                    public ModernNetworking.Env getEnv() {
                        return ModernNetworking.Env.CLIENT;
                    }

                    @Override
                    public void queue(Runnable runnable) {
                        client.execute(runnable);
                    }
                }, data);
            });
        }
    }

    @ApiStatus.Internal
    public static Packet<?> toPacket(ModernNetworking.Side side, ResourceLocation id, FriendlyByteBuf buf) {
        return switch (side) {
            case C2S -> toC2SPacket(id, buf);
            case S2C -> toS2CPacket(id, buf);
        };
    }

    @Environment(EnvType.CLIENT)
    private static Packet<?> toC2SPacket(ResourceLocation id, FriendlyByteBuf buf) {
        return new ServerboundCustomPayloadPacket(buf);
    }

    private static Packet<?> toS2CPacket(ResourceLocation id, FriendlyByteBuf buf) {
        return new ClientboundCustomPayloadPacket(buf);
    }
}
//#endif
