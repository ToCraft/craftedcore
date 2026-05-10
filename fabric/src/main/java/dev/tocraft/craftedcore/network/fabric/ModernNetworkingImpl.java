package dev.tocraft.craftedcore.network.fabric;

import dev.tocraft.craftedcore.network.ModernNetworking;
import dev.tocraft.craftedcore.network.ModernNetworking.PacketPayload;
import dev.tocraft.craftedcore.network.ModernNetworkingService;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static dev.tocraft.craftedcore.network.ModernNetworking.getType;

@SuppressWarnings({"unused", "resource"})
public class ModernNetworkingImpl implements ModernNetworkingService {
    @Override
    public void registerReceiver(ModernNetworking.Side side, Identifier id, ModernNetworking.Receiver receiver) {
        if (side == ModernNetworking.Side.C2S) {
            PayloadTypeRegistry.serverboundPlay().register(getType(id), PacketPayload.streamCodec());
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
                    MinecraftServer server = context.server();
                    if (server != null) {
                        server.execute(runnable);
                    }
                }
            }, payload.nbt()));
        } else if (side == ModernNetworking.Side.S2C) {
            PayloadTypeRegistry.clientboundPlay().register(getType(id), PacketPayload.streamCodec());
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

    @Override
    public void registerType(Identifier id) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            ModernNetworking.getType(id);
            PayloadTypeRegistry.clientboundPlay().register(getType(id), PacketPayload.streamCodec());
        }
    }

    @Contract("_, _ -> new")
    @Override
    @ApiStatus.Internal
    public @NotNull Packet<?> toPacket(ModernNetworking.Side side, CustomPacketPayload payload) {
        if (side == ModernNetworking.Side.C2S) {
            return new ServerboundCustomPayloadPacket(payload);
        } else {
            return new ClientboundCustomPayloadPacket(payload);
        }
    }
}
