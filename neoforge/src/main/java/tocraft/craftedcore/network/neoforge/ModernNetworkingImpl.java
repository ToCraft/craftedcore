package tocraft.craftedcore.network.neoforge;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.network.ModernNetworking;

import java.util.Objects;

@SuppressWarnings("unused")
public class ModernNetworkingImpl {
    public static void registerReceiver(ModernNetworking.Side side, ResourceLocation id, ModernNetworking.Receiver
            receiver) {
        IEventBus eventBus = Objects.requireNonNull(ModList.get().getModContainerById(CraftedCore.MODID).orElseThrow().getEventBus());

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
}
