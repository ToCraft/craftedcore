package tocraft.craftedcore.events.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import tocraft.craftedcore.events.Event;
import tocraft.craftedcore.events.EventBuilder;

public interface ClientTickEvents<T> {
	
	 Event<Client> CLIENT_PRE = EventBuilder.createLoop();
	 Event<Client> CLIENT_POST = EventBuilder.createLoop();

	 void tick(T instance);
	 
	 @Environment(EnvType.CLIENT)
    interface Client extends ClientTickEvents<Minecraft> {
    }
}
