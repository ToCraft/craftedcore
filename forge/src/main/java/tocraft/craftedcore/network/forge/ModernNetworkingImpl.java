package tocraft.craftedcore.network.forge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.EventNetworkChannel;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.network.ModernNetworking;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class ModernNetworkingImpl {
    private static final EventNetworkChannel CHANNEL = ChannelBuilder.named(CraftedCore.id("network")).acceptedVersions((status, version) -> true).optional().eventNetworkChannel();
    private static final Map<ResourceLocation, ModernNetworking.Receiver> C2S_RECEIVER = new HashMap<>();
    private static final Map<ResourceLocation, ModernNetworking.Receiver> S2C_RECEIVER = new HashMap<>();

    public static void initialize() {
        CHANNEL.addListener(event -> {
            FriendlyByteBuf buf = event.getPayload();
            CompoundTag payload = buf.readNbt();
            ModernNetworking.Context context = new ModernNetworking.Context() {
                @Override
                public Player getPlayer() {
                    return event.getSource().getSender();
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
                receiver = S2C_RECEIVER.get(event.getChannel());
            } else {
                receiver = C2S_RECEIVER.get(event.getChannel());
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
}
