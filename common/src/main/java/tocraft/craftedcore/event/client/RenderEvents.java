package tocraft.craftedcore.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import tocraft.craftedcore.event.Event;
import tocraft.craftedcore.event.EventFactory;

@SuppressWarnings({"unused", "SameReturnValue"})
@Environment(EnvType.CLIENT)
public final class RenderEvents {
    public static final Event<HUDRendering> HUD_RENDERING = EventFactory.createWithVoid();
    public static final Event<OverlayRendering> RENDER_HEALTH = EventFactory.createWithInteractionResult();
    public static final Event<OverlayRendering> RENDER_FOOD = EventFactory.createWithInteractionResult();
    public static final Event<OverlayRendering> RENDER_BREATH = EventFactory.createWithInteractionResult();
    public static final Event<OverlayRendering> RENDER_MOUNT_HEALTH = EventFactory.createWithInteractionResult();

    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    public interface HUDRendering {
        void render(PoseStack poseStack, float tickDelta);
    }

    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    public interface OverlayRendering {
        InteractionResult render(@Nullable PoseStack poseStack, Player player);
    }
}
