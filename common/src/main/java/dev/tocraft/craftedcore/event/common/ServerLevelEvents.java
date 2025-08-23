package dev.tocraft.craftedcore.event.common;

import dev.tocraft.craftedcore.event.Event;
import dev.tocraft.craftedcore.event.EventFactory;
import net.minecraft.server.level.ServerLevel;

@SuppressWarnings("unused")
public final class ServerLevelEvents {
    public static final Event<LevelEvent> LEVEL_LOAD = EventFactory.createWithVoid();
    public static final Event<LevelEvent> LEVEL_UNLOAD = EventFactory.createWithVoid();

    @FunctionalInterface
    public interface LevelEvent {
        void call(ServerLevel level);
    }
}
