package tocraft.craftedcore.event.common;

import net.minecraft.server.level.ServerLevel;
import tocraft.craftedcore.event.Event;
import tocraft.craftedcore.event.EventFactory;

@SuppressWarnings("unused")
public final class ServerLevelEvents {
    public static final Event<LevelEvent> LEVEL_LOAD = EventFactory.createWithVoid();
    public static final Event<LevelEvent> LEVEL_UNLOAD = EventFactory.createWithVoid();

    @FunctionalInterface
    public interface LevelEvent {
        void call(ServerLevel level);
    }
}
