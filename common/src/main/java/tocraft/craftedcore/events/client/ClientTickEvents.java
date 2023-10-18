package tocraft.craftedcore.events.client;

import net.minecraft.client.Minecraft;
import tocraft.craftedcore.events.Event;
import tocraft.craftedcore.events.EventBuilder;

public interface ClientTickEvents<T> {
	
	 Event<Client> CLIENT_PRE = EventBuilder.createLoop();
	 Event<Client> CLIENT_POST = EventBuilder.createLoop();

	 void tick(T instance);
	 
    interface Client extends ClientTickEvents<Minecraft> {
    }
}
