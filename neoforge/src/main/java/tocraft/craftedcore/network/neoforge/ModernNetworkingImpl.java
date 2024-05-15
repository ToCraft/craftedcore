package tocraft.craftedcore.network.neoforge;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import org.jetbrains.annotations.ApiStatus;
import tocraft.craftedcore.neoforge.CraftedCoreNeoForge;
import tocraft.craftedcore.network.ModernNetworking;

@SuppressWarnings("unused")
public class ModernNetworkingImpl {
    public static void registerReceiver(ModernNetworking.Side side, ResourceLocation id, ModernNetworking.Receiver
            receiver) {
        IEventBus eventBus = CraftedCoreNeoForge.getEventBus();

        eventBus.addListener(RegisterPayloadHandlerEvent.class, event -> event.registrar(id.getNamespace()).play(id, ModernNetworking.PacketPayload::new, builder -> {
            if (side == ModernNetworking.Side.C2S) {
                builder.server((arg, context) -> receiver.receive(new ModernNetworking.Context() {
                    @Override
                    public Player getPlayer() {
                        return context.player().orElse(null);
                    }

                    @Override
                    public ModernNetworking.Env getEnv() {
                        return ModernNetworking.Env.SERVER;
                    }

                    @Override
                    public void queue(Runnable runnable) {
                        context.workHandler().execute(runnable);
                    }
                }, arg.nbt()));
            } else {
                builder.client((arg, context) -> receiver.receive(new ModernNetworking.Context() {
                    @Override
                    public Player getPlayer() {
                        return Minecraft.getInstance().player;
                    }

                    @Override
                    public ModernNetworking.Env getEnv() {
                        return ModernNetworking.Env.CLIENT;
                    }

                    @Override
                    public void queue(Runnable runnable) {
                        context.workHandler().execute(runnable);
                    }
                }, arg.nbt()));
            }
        }));
    }

    @ApiStatus.Internal
    public static Packet<?> toPacket(ModernNetworking.Side side, ResourceLocation id, FriendlyByteBuf buf) {
        if (side == ModernNetworking.Side.C2S) {
            return new ClientboundCustomPayloadPacket(new ModernNetworking.PacketPayload(buf));
        } else {
            return new ServerboundCustomPayloadPacket(new ModernNetworking.PacketPayload(buf));
        }
    }
}
