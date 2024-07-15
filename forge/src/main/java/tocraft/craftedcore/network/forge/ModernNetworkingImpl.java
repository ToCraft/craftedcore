package tocraft.craftedcore.network.forge;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkDirection;
//#if MC>1201
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.EventNetworkChannel;
import net.minecraftforge.event.network.CustomPayloadEvent;
//#else
//$$ import net.minecraftforge.network.NetworkEvent;
//$$ import net.minecraftforge.network.NetworkRegistry;
//$$ import net.minecraftforge.network.event.EventNetworkChannel;
//$$ import org.apache.commons.lang3.tuple.Pair;
//#endif
import org.jetbrains.annotations.ApiStatus;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.network.ModernNetworking;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class ModernNetworkingImpl {
    private static final ResourceLocation CHANNEL_ID = CraftedCore.id("network");
    //#if MC>1201
    private static final EventNetworkChannel CHANNEL = ChannelBuilder.named(CHANNEL_ID).acceptedVersions((status, version) -> true).optional().eventNetworkChannel();
    //#else
    //$$ private static final EventNetworkChannel CHANNEL = NetworkRegistry.newEventChannel(CHANNEL_ID, () -> "1", version -> true, version -> true);
    //#endif
    private static final Map<ResourceLocation, ModernNetworking.Receiver> C2S_RECEIVER = new HashMap<>();
    private static final Map<ResourceLocation, ModernNetworking.Receiver> S2C_RECEIVER = new HashMap<>();

    public static void initialize() {
        CHANNEL.addListener(event -> {
            FriendlyByteBuf buf = event.getPayload();
            //#if MC>1201
            CustomPayloadEvent.Context source = event.getSource();
            //#else
            //$$ NetworkEvent.Context source = event.getSource().get();
            //#endif
            if (buf == null || source.getPacketHandled()) return;
            ResourceLocation packetId = buf.readResourceLocation();
            CompoundTag payload = buf.readNbt();
            ModernNetworking.Context context = new ModernNetworking.Context() {
                @Override
                public Player getPlayer() {
                    return getEnv() == ModernNetworking.Env.CLIENT ? DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().player) : source.getSender();
                }

                @Override
                public ModernNetworking.Env getEnv() {
                    return source.getDirection().getReceptionSide() == LogicalSide.CLIENT ? ModernNetworking.Env.CLIENT : ModernNetworking.Env.SERVER;
                }

                @Override
                public void queue(Runnable runnable) {
                    source.enqueueWork(runnable);
                }
            };

            ModernNetworking.Receiver receiver;
            if (context.getEnv() == ModernNetworking.Env.CLIENT) {
                receiver = S2C_RECEIVER.get(packetId);
            } else {
                receiver = C2S_RECEIVER.get(packetId);
            }
            receiver.receive(context, payload);
            source.setPacketHandled(true);
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
    public static Packet<?> toPacket(ModernNetworking.Side side, ResourceLocation id, CompoundTag data) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeResourceLocation(id);
        buf.writeNbt(data);
        NetworkDirection direction = side == ModernNetworking.Side.C2S ? NetworkDirection.PLAY_TO_SERVER : NetworkDirection.PLAY_TO_CLIENT;
        //#if MC>1201
        return direction.buildPacket(buf, CHANNEL_ID).getThis();
        //#else
        //$$ return direction.buildPacket(Pair.of(buf, 0), CHANNEL_ID).getThis();
        //#endif
    }
}
