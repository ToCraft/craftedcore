package dev.tocraft.craftedcore.network.neoforge;

import dev.tocraft.craftedcore.neoforge.CraftedCoreNeoForge;
import dev.tocraft.craftedcore.network.ModernNetworking;
import dev.tocraft.craftedcore.network.ModernNetworking.PacketPayload;
import dev.tocraft.craftedcore.network.ModernNetworkingService;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static dev.tocraft.craftedcore.network.ModernNetworking.getType;

@SuppressWarnings("unused")
public class ModernNetworkingImpl implements ModernNetworkingService {
    @Override
    public void registerReceiver(ModernNetworking.Side side, Identifier id, ModernNetworking.Receiver receiver) {
        IEventBus eventBus = CraftedCoreNeoForge.getEventBus();

        if (side == ModernNetworking.Side.C2S) {
            eventBus.addListener(RegisterPayloadHandlersEvent.class, event -> event.registrar(id.getNamespace()).playToServer(getType(id), PacketPayload.streamCodec(), (arg, context) -> receiver.receive(new ModernNetworking.Context() {
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
                    context.enqueueWork(runnable);
                }
            }, arg.nbt())));
        } else if (side == ModernNetworking.Side.S2C) {
            eventBus.addListener(RegisterPayloadHandlersEvent.class, event -> event.registrar(id.getNamespace()).playToClient(getType(id), PacketPayload.streamCodec(), (arg, context) -> receiver.receive(new ModernNetworking.Context() {
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
                    context.enqueueWork(runnable);
                }
            }, arg.nbt())));
        }
    }

    @Override
    public void registerType(Identifier id) {
        if (FMLEnvironment.getDist().isDedicatedServer()) {
            ModernNetworking.getType(id);
            registerReceiver(ModernNetworking.Side.S2C, id, (context, data) -> {
            });
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
