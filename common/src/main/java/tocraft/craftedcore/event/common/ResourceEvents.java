package tocraft.craftedcore.event.common;

import net.minecraft.server.level.ServerPlayer;
import tocraft.craftedcore.event.Event;
import tocraft.craftedcore.event.EventFactory;

@SuppressWarnings("unused")
public final class ResourceEvents {
    public static final Event<DataPackSync> DATA_PACK_SYNC = EventFactory.createWithVoid();

    @FunctionalInterface
    public interface DataPackSync {
        void onSync(ServerPlayer player);
    }
}
