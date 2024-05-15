package tocraft.craftedcore.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("unused")
public class TimerOverlayRenderer {

    private static final int fadingTickRequirement = 0;
    private static int ticksSinceUpdate = 0;
    private static boolean isFading = false;
    private static int fadingProgress = 0;

    public static void register(PoseStack graphics, int currentCooldown, int maxCooldown, Item item) {
        Minecraft client = Minecraft.getInstance();

        if (client.screen instanceof ChatScreen || currentCooldown <= 0 || item == null) {
            return;
        }

        double d = client.getWindow().getGuiScale();
        float cooldownScale = 1 - currentCooldown / (float) maxCooldown;

        // cooldown has NOT updated since last tick. It is most likely full.
        if (ticksSinceUpdate > fadingProgress) {
            ticksSinceUpdate = 0;
            isFading = false;
        }

        // Tick fading
        if (isFading) {
            fadingProgress = Math.min(50, fadingProgress + 1);
        } else {
            fadingProgress = Math.max(0, fadingProgress - 1);
        }

        if (client.player != null) {
            int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            graphics.pushPose();
            if (cooldownScale != 1) {
                RenderSystem.enableScissor(
                        (int) ((double) 0 * d),
                        (int) ((double) 0 * d),
                        (int) ((double) width * d),
                        (int) ((double) height * (.02 + .055 * cooldownScale) * d)); // min is 0.21, max is 0.76. dif = .55
            }

            // ending pop
            if (isFading) {
                float fadeScalar = fadingProgress / 50f; // 0f -> 1f, 0 is start, 1 is end
                float scale = 1f + (float) Math.sin(fadeScalar * 1.5 * Math.PI) - .25f;
                scale = Math.max(scale, 0);
                graphics.scale(scale, scale, scale);
            }

            ItemStack stack = new ItemStack(item);
            Minecraft.getInstance().getItemRenderer().renderGuiItem(stack, (int) (width * .95f), (int) (height * .92f));

            RenderSystem.disableScissor();
            graphics.popPose();
        }
    }
}
