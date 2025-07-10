package dev.tocraft.craftedcore.network.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import dev.tocraft.craftedcore.network.ModernNetworking;
import dev.tocraft.craftedcore.network.ModernNetworking.PacketPayload;

import static dev.tocraft.craftedcore.network.ModernNetworking.getType;

@SuppressWarnings({"unused", "resource"})
public class ModernNetworkingImpl {
    public static void registerReceiver(ModernNetworking.Side side, ResourceLocation id, ModernNetworking.Receiver receiver) {
        if (side == ModernNetworking.Side.C2S) {
            PayloadTypeRegistry.playC2S().register(getType(id), PacketPayload.streamCodec());
            ServerPlayNetworking.registerGlobalReceiver(getType(id), (payload, context) -> receiver.receive(new ModernNetworking.Context() {
                @Override
                public Player getPlayer() {
                    return context.player();
                }

                @Override
                public ModernNetworking.Env getEnv() {
                    return ModernNetworking.Env.SERVER;
                }

                @Override
                public void queue(Runnable runnable) {
                    MinecraftServer server = context.player().getServer();
                    if (server != null) {
                        server.execute(runnable);
                    }
                }
            }, payload.nbt()));
        } else if (side == ModernNetworking.Side.S2C) {
            PayloadTypeRegistry.playS2C().register(getType(id), PacketPayload.streamCodec());
            ClientPlayNetworking.registerGlobalReceiver(getType(id), (payload, context) -> receiver.receive(new ModernNetworking.Context() {
                @Override
                public Player getPlayer() {
                    return context.player();
                }

                @Override
                public ModernNetworking.Env getEnv() {
                    return ModernNetworking.Env.CLIENT;
                }

                @Override
                public void queue(Runnable runnable) {
                    context.client().execute(runnable);
                }
            }, payload.nbt()));
        }
    }

    public static void registerType(ResourceLocation id) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            ModernNetworking.getType(id);
            PayloadTypeRegistry.playS2C().register(getType(id), PacketPayload.streamCodec());
        }
    }

    @Contract("_, _ -> new")
    @ApiStatus.Internal
    public static @NotNull Packet<?> toPacket(ModernNetworking.Side side, CustomPacketPayload payload) {
        if (side == ModernNetworking.Side.C2S) {
            return new ServerboundCustomPayloadPacket(payload);
        } else {
            return new ClientboundCustomPayloadPacket(payload);
        }
    }
}

