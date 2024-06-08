package tocraft.craftedcore.event.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import tocraft.craftedcore.event.Event;
import tocraft.craftedcore.event.EventFactory;

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
