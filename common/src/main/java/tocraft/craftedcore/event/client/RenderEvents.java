package tocraft.craftedcore.event.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import tocraft.craftedcore.event.Event;
import tocraft.craftedcore.event.EventFactory;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class RenderEvents {
    public static final Event<HUDRendering> HUD_RENDERING = EventFactory.createWithVoid();

    public interface HUDRendering {
        void render(GuiGraphics graphics, float tickDelta);
    }

    public enum Overlay {
        HEALTH, FOOD, BREATH, EXPERIENCE, MOUNT_HEALTH
    }
}
