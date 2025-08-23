package dev.tocraft.craftedcore.event.client;

import dev.tocraft.craftedcore.event.Event;
import dev.tocraft.craftedcore.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class ClientTickEvents {
    public static final Event<Client> CLIENT_PRE = EventFactory.createWithVoid();
    public static final Event<Client> CLIENT_POST = EventFactory.createWithVoid();

    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    public interface Client {
        void tick(Minecraft instance);
    }
}
