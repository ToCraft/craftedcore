package tocraft.craftedcore.event;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import tocraft.craftedcore.event.client.ClientTickEvents;
import tocraft.craftedcore.event.common.ServerLevelEvents;

public final class ArchitecturyImpl {
    public static void initialize() {
        LifecycleEvent.SERVER_LEVEL_LOAD.register(world -> ServerLevelEvents.LEVEL_LOAD.invoke().call(world));
        LifecycleEvent.SERVER_LEVEL_UNLOAD.register(world -> ServerLevelEvents.LEVEL_UNLOAD.invoke().call(world));
    }

    @Environment(EnvType.CLIENT)
    public static void clientInitialize() {
        ClientTickEvent.CLIENT_PRE.register(instance -> ClientTickEvents.CLIENT_PRE.invoke().tick(instance));
        ClientTickEvent.CLIENT_POST.register(instance -> ClientTickEvents.CLIENT_POST.invoke().tick(instance));
        ClientTickEvent.CLIENT_LEVEL_PRE.register(instance -> ClientTickEvents.CLIENT_LEVEL_PRE.invoke().tick(instance));
        ClientTickEvent.CLIENT_LEVEL_POST.register(instance -> ClientTickEvents.CLIENT_LEVEL_POST.invoke().tick(instance));
    }
}
