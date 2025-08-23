package dev.tocraft.craftedcore.event.common;

import dev.tocraft.craftedcore.event.Event;
import dev.tocraft.craftedcore.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

@SuppressWarnings("unused")
public final class ResourceEvents {
    public static final Event<DataPackSync> DATA_PACK_SYNC = EventFactory.createWithVoid();

    @FunctionalInterface
    public interface DataPackSync {
        void onSync(ServerPlayer player);
    }
}
