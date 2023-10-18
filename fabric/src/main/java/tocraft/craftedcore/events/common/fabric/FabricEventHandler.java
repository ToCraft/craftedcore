package tocraft.craftedcore.events.common.fabric;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import tocraft.craftedcore.events.common.CommandEvents;

public class FabricEventHandler {

	public FabricEventHandler() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registry, selection) -> CommandEvents.REGISTRATION.invoker().register(dispatcher, registry, selection));
	}
}
