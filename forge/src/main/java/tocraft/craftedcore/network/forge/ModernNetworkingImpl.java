package tocraft.craftedcore.network.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.EventNetworkChannel;
import net.minecraftforge.network.NetworkDirection;
import org.jetbrains.annotations.ApiStatus;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.network.ModernNetworking;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class ModernNetworkingImpl {
    private static final ResourceLocation CHANNEL_ID = CraftedCore.id("network");
    private static final EventNetworkChannel CHANNEL = ChannelBuilder.named(CHANNEL_ID).acceptedVersions((status, version) -> true).optional().eventNetworkChannel();
    private static final Map<ResourceLocation, ModernNetworking.Receiver> C2S_RECEIVER = new HashMap<>();
    private static final Map<ResourceLocation, ModernNetworking.Receiver> S2C_RECEIVER = new HashMap<>();

    public static void initialize() {
        CHANNEL.addListener(event -> {
            FriendlyByteBuf buf = event.getPayload();
            if (buf == null || event.getSource().getPacketHandled()) return;
            ResourceLocation packetId = buf.readResourceLocation();
            CompoundTag payload = buf.readNbt();
            ModernNetworking.Context context = new ModernNetworking.Context() {
                @Override
                public Player getPlayer() {
                    return getEnv() == ModernNetworking.Env.CLIENT ? DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().player) : event.getSource().getSender();
                }

                @Override
                public ModernNetworking.Env getEnv() {
                    return event.getSource().getDirection().getReceptionSide() == LogicalSide.CLIENT ? ModernNetworking.Env.CLIENT : ModernNetworking.Env.SERVER;
                }

                @Override
                public void queue(Runnable runnable) {
                    event.getSource().enqueueWork(runnable);
                }
            };

            ModernNetworking.Receiver receiver;
            if (context.getEnv() == ModernNetworking.Env.CLIENT) {
                receiver = S2C_RECEIVER.get(packetId);
            } else {
                receiver = C2S_RECEIVER.get(packetId);
            }
            receiver.receive(context, payload);
            event.getSource().setPacketHandled(true);
        });
    }

    public static void registerReceiver(ModernNetworking.Side side, ResourceLocation id, ModernNetworking.Receiver
            receiver) {
        if (side == ModernNetworking.Side.C2S) {
            C2S_RECEIVER.put(id, receiver);
        } else if (side == ModernNetworking.Side.S2C) {
            S2C_RECEIVER.put(id, receiver);
        }
    }

    @ApiStatus.Internal
    public static Packet<?> toPacket(ModernNetworking.Side side, ResourceLocation id, FriendlyByteBuf buf) {
        if (side == ModernNetworking.Side.C2S) {
            return NetworkDirection.PLAY_TO_SERVER.buildPacket(buf, CHANNEL_ID).getThis();
        } else {
            return NetworkDirection.PLAY_TO_CLIENT.buildPacket(buf, CHANNEL_ID).getThis();
        }
    }
}
