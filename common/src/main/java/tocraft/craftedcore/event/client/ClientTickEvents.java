package tocraft.craftedcore.event.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import tocraft.craftedcore.event.Event;
import tocraft.craftedcore.event.EventFactory;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class ClientTickEvents {
    public static final Event<Client> CLIENT_PRE = EventFactory.createWithVoid();
    public static final Event<Client> CLIENT_POST = EventFactory.createWithVoid();
    public static final Event<ClientWorld> CLIENT_LEVEL_PRE = EventFactory.createWithVoid();
    public static final Event<ClientWorld> CLIENT_LEVEL_POST = EventFactory.createWithVoid();

    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    public interface Client {
        void tick(Minecraft instance);
    }

    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    public interface ClientWorld {
        void tick(ClientLevel level);
    }
}
