package tocraft.craftedcore.network.neoforge;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.network.ModernNetworking;
import tocraft.craftedcore.network.ModernNetworking.PacketPayload;

import java.util.Objects;

import static tocraft.craftedcore.network.ModernNetworking.getType;

@SuppressWarnings("unused")
public class ModernNetworkingImpl {
    public static void registerReceiver(ModernNetworking.Side side, ResourceLocation id, ModernNetworking.Receiver
            receiver) {
        IEventBus eventBus = Objects.requireNonNull(ModList.get().getModContainerById(CraftedCore.MODID).orElseThrow().getEventBus());

        if (side == ModernNetworking.Side.C2S) {
            eventBus.addListener(RegisterPayloadHandlersEvent.class, event -> event.registrar(id.toString()).playToServer(getType(id), PacketPayload.streamCodec(), (arg, context) -> receiver.receive(new ModernNetworking.Context() {
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
            eventBus.addListener(RegisterPayloadHandlersEvent.class, event -> event.registrar(id.toString()).playToClient(getType(id), PacketPayload.streamCodec(), (arg, context) -> receiver.receive(new ModernNetworking.Context() {
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
}
