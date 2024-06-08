package tocraft.craftedcore.network.fabric;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
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
        if (side == ModernNetworking.Side.C2S) {
            return new ServerboundCustomPayloadPacket(buf);
        } else {
            return new ClientboundCustomPayloadPacket(buf);
        }
    }
}
