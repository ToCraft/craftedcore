package tocraft.craftedcore.network.fabric;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import tocraft.craftedcore.network.ModernNetworking;
import tocraft.craftedcore.network.ModernNetworking.PacketPayload;

import static tocraft.craftedcore.network.ModernNetworking.getType;

@SuppressWarnings("unused")
public class ModernNetworkingImpl {
    public static void registerReceiver(ModernNetworking.Side side, ResourceLocation id, ModernNetworking.Receiver receiver) {
        if (side == ModernNetworking.Side.C2S) {
            PayloadTypeRegistry.playC2S().register(getType(id), PacketPayload.streamCodec(getType(id)));
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
                    context.player().server.execute(runnable);
                }
            }, payload.nbt()));
        } else if (side == ModernNetworking.Side.S2C) {
            PayloadTypeRegistry.playS2C().register(getType(id), PacketPayload.streamCodec(getType(id)));
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
}
