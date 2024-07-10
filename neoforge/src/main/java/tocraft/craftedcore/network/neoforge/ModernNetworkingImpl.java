package tocraft.craftedcore.network.neoforge;
//#if MC>=1205
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.jetbrains.annotations.ApiStatus;
import tocraft.craftedcore.neoforge.CraftedCoreNeoForge;
import tocraft.craftedcore.network.ModernNetworking;
import tocraft.craftedcore.network.ModernNetworking.PacketPayload;

import static tocraft.craftedcore.network.ModernNetworking.getType;

@SuppressWarnings("unused")
public class ModernNetworkingImpl {
    public static void registerReceiver(ModernNetworking.Side side, ResourceLocation id, ModernNetworking.Receiver
            receiver) {
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

    public static void registerType(ResourceLocation id) {
        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER) {
            ModernNetworking.getType(id);
            registerReceiver(ModernNetworking.Side.S2C, id, (context, data) -> {
            });
        }
    }

    @ApiStatus.Internal
    public static Packet<?> toPacket(ModernNetworking.Side side, CustomPacketPayload payload) {
        if (side == ModernNetworking.Side.C2S) {
            return new ServerboundCustomPayloadPacket(payload);
        } else {
            return new ClientboundCustomPayloadPacket(payload);
        }
    }
}
//#elseif MC>1202
//$$ import net.minecraft.client.Minecraft;
//$$ import net.minecraft.nbt.CompoundTag;
//$$ import net.minecraft.network.FriendlyByteBuf;
//$$ import net.minecraft.network.protocol.Packet;
//$$ import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
//$$ import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
//$$ import net.minecraft.resources.ResourceLocation;
//$$ import net.minecraft.world.entity.player.Player;
//$$ import net.neoforged.bus.api.IEventBus;
//$$ import net.neoforged.fml.LogicalSide;
//$$ import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
//$$ import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
//$$ import org.jetbrains.annotations.ApiStatus;
//$$ import tocraft.craftedcore.CraftedCore;
//$$ import tocraft.craftedcore.network.ModernNetworking;
//$$
//$$ import java.util.HashMap;
//$$ import java.util.Map;
//$$
//$$ @SuppressWarnings("unused")
//$$ public class ModernNetworkingImpl {
//$$     public static final ResourceLocation CHANNEL_ID = CraftedCore.id("network");
//$$     private static final Map<ResourceLocation, ModernNetworking.Receiver> S2C = new HashMap<>();
//$$     private static final Map<ResourceLocation, ModernNetworking.Receiver> C2S = new HashMap<>();
//$$
//$$     public static void initialize(IEventBus eventBus) {
//$$         eventBus.addListener(RegisterPayloadHandlerEvent.class, event -> {
//$$             IPayloadRegistrar registrar = event.registrar(CraftedCore.MODID).optional();
//$$
//$$             registrar.play(CHANNEL_ID, ModernNetworking.PacketPayload::new, builder -> builder.server((arg, context) -> C2S.get(arg.id()).receive(new ModernNetworking.Context() {
//$$                 @Override
//$$                 public Player getPlayer() {
//$$                     return context.player().orElse(null);
//$$                 }
//$$
//$$                 @Override
//$$                 public ModernNetworking.Env getEnv() {
//$$                     return ModernNetworking.Env.SERVER;
//$$                 }
//$$
//$$                 @Override
//$$                 public void queue(Runnable runnable) {
//$$                     context.workHandler().execute(runnable);
//$$                 }
//$$             }, arg.nbt())));
//$$
//$$         registrar.play(CHANNEL_ID, ModernNetworking.PacketPayload::new, builder -> builder.client((arg, context) -> S2C.get(arg.id()).receive(new ModernNetworking.Context() {
//$$             @Override
//$$             public Player getPlayer() {
//$$                 return Minecraft.getInstance().player;
//$$             }
//$$
//$$             @Override
//$$             public ModernNetworking.Env getEnv() {
//$$                 return ModernNetworking.Env.CLIENT;
//$$             }
//$$
//$$             @Override
//$$             public void queue(Runnable runnable) {
//$$                 context.workHandler().execute(runnable);
//$$             }
//$$         }, arg.nbt())));
//$$     });
//$$     }
//$$
//$$     public static void registerReceiver(ModernNetworking.Side side, ResourceLocation id, ModernNetworking.Receiver
//$$             receiver) {
//$$         switch (side) {
//$$             case S2C -> S2C.put(id, receiver);
//$$             case C2S -> C2S.put(id, receiver);
//$$         }
//$$     }
//$$
//$$     @ApiStatus.Internal
//$$     public static Packet<?> toPacket(ModernNetworking.Side side, ResourceLocation id, FriendlyByteBuf buf) {
//$$         if (side == ModernNetworking.Side.C2S) {
//$$             return new ServerboundCustomPayloadPacket(new ModernNetworking.PacketPayload(buf));
//$$         } else {
//$$             return new ClientboundCustomPayloadPacket(new ModernNetworking.PacketPayload(buf));
//$$         }
//$$     }
//$$ }
//#else
//$$ import net.minecraft.client.Minecraft;
//$$ import net.minecraft.nbt.CompoundTag;
//$$ import net.minecraft.network.FriendlyByteBuf;
//$$ import net.minecraft.network.protocol.Packet;
//$$ import net.minecraft.resources.ResourceLocation;
//$$ import net.minecraft.world.entity.player.Player;
//$$ import net.neoforged.fml.LogicalSide;
//$$ import net.neoforged.neoforge.network.INetworkDirection;
//$$ import net.neoforged.neoforge.network.NetworkRegistry;
//$$ import net.neoforged.neoforge.network.PlayNetworkDirection;
//$$ import net.neoforged.neoforge.network.event.EventNetworkChannel;
//$$ import org.jetbrains.annotations.ApiStatus;
//$$ import tocraft.craftedcore.CraftedCore;
//$$ import tocraft.craftedcore.network.ModernNetworking;
//$$
//$$ import java.util.HashMap;
//$$ import java.util.Map;
//$$
//$$ @SuppressWarnings("unused")
//$$ public class ModernNetworkingImpl {
//$$     private static final ResourceLocation CHANNEL_ID = CraftedCore.id("network");
//$$     private static final EventNetworkChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(CHANNEL_ID).networkProtocolVersion(() -> "").clientAcceptedVersions(version -> true).serverAcceptedVersions(version -> true).eventNetworkChannel();
//$$     private static final Map<ResourceLocation, ModernNetworking.Receiver> C2S_RECEIVER = new HashMap<>();
//$$     private static final Map<ResourceLocation, ModernNetworking.Receiver> S2C_RECEIVER = new HashMap<>();
//$$
//$$     public static void initialize() {
//$$         CHANNEL.addListener(event -> {
//$$             FriendlyByteBuf buf = event.getPayload();
//$$             if (buf == null || event.getSource().getPacketHandled()) return;
//$$             ResourceLocation packetId = buf.readResourceLocation();
//$$             CompoundTag payload = buf.readNbt();
//$$             ModernNetworking.Context context = new ModernNetworking.Context() {
//$$                 @Override
//$$                 public Player getPlayer() {
//$$                     return getEnv() == ModernNetworking.Env.CLIENT ? Minecraft.getInstance().player : event.getSource().getSender();
//$$                 }
//$$
//$$                 @Override
//$$                 public ModernNetworking.Env getEnv() {
//$$                     return event.getSource().getDirection().getReceptionSide() == LogicalSide.CLIENT ? ModernNetworking.Env.CLIENT : ModernNetworking.Env.SERVER;
//$$                 }
//$$
//$$                 @Override
//$$                 public void queue(Runnable runnable) {
//$$                     event.getSource().enqueueWork(runnable);
//$$                 }
//$$             };
//$$
//$$             ModernNetworking.Receiver receiver;
//$$             if (context.getEnv() == ModernNetworking.Env.CLIENT) {
//$$                 receiver = S2C_RECEIVER.get(packetId);
//$$             } else {
//$$                 receiver = C2S_RECEIVER.get(packetId);
//$$             }
//$$             receiver.receive(context, payload);
//$$             event.getSource().setPacketHandled(true);
//$$         });
//$$     }
//$$
//$$     public static void registerReceiver(ModernNetworking.Side side, ResourceLocation id, ModernNetworking.Receiver
//$$             receiver) {
//$$         if (side == ModernNetworking.Side.C2S) {
//$$             C2S_RECEIVER.put(id, receiver);
//$$         } else if (side == ModernNetworking.Side.S2C) {
//$$             S2C_RECEIVER.put(id, receiver);
//$$         }
//$$     }
//$$
//$$     @ApiStatus.Internal
//$$     public static Packet<?> toPacket(ModernNetworking.Side side, ResourceLocation id, FriendlyByteBuf buf) {
//$$         if (side == ModernNetworking.Side.C2S) {
//$$             return PlayNetworkDirection.PLAY_TO_SERVER.buildPacket(new INetworkDirection.PacketData(buf, 0), CHANNEL_ID);
//$$         } else {
//$$             return PlayNetworkDirection.PLAY_TO_CLIENT.buildPacket(new INetworkDirection.PacketData(buf, 0), CHANNEL_ID);
//$$         }
//$$     }
//$$ }
//#endif
