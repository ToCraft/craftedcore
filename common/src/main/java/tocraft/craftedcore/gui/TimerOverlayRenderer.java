package tocraft.craftedcore.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TimerOverlayRenderer {

    private static final int fadingTickRequirement = 0;
    private static int lastCooldown = 0;
    private static int ticksSinceUpdate = 0;
    private static boolean isFading = false;
    private static int fadingProgress = 0;

    public static void register(GuiGraphics graphics, int currentCooldown, int maxCooldown) {
        
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;

        if(client.screen instanceof ChatScreen) {
            return;
        }

        double d = client.getWindow().getGuiScale();
        float cooldownScale = 1 - currentCooldown / maxCooldown;

        // CD has NOT updated since last tick. It is most likely full.
        if(currentCooldown == lastCooldown) {
            ticksSinceUpdate++;

            // If the CD has not updated, we are above the requirement, and we are not fading, start fading.
            if(ticksSinceUpdate > fadingTickRequirement && !isFading) {
                isFading = true;
                fadingProgress = 0;
            }
        }

        // CD updated in the last tick, and we are fading. Stop fading.
        else if(ticksSinceUpdate > fadingProgress) {
            ticksSinceUpdate = 0;
            isFading = false;
        }

        // Tick fading
        if(isFading) {
            fadingProgress = Math.min(50, fadingProgress + 1);
        } else {
            fadingProgress = Math.max(0, fadingProgress - 1);
        }

        if(player != null) {
            int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            graphics.pose().pushPose();
            if(cooldownScale != 1) {
                RenderSystem.enableScissor(
                        (int) ((double) 0 * d),
                        (int) ((double) 0 * d),
                        (int) ((double) width * d),
                        (int) ((double) height * (.02 + .055 * cooldownScale) * d)); // min is 0.21, max is 0.76. dif = .55
            }

            // ending pop
            if(isFading) {
                float fadeScalar = fadingProgress / 50f; // 0f -> 1f, 0 is start, 1 is end
                float scale = 1f + (float) Math.sin(fadeScalar * 1.5 * Math.PI) - .25f;
                scale = Math.max(scale, 0);
                graphics.pose().scale(scale, scale, scale);
            }

            // TODO: cache ability stack?
//                MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(new ItemStack(shapeAbility.getIcon()), (int) (width * .95f), (int) (height * .92f));
            ItemStack stack = new ItemStack(Items.GOLDEN_APPLE);
//                BakedModel heldItemModel = MinecraftClient.getInstance().getItemRenderer().getHeldItemModel(stack, client.world, player);
//                renderGuiItemModel(matrices, stack, (int) (width * .95f), (int) (height * .92f), heldItemModel);
            graphics.renderItem(stack, (int) (width * .95f), (int) (height * .92f));

            RenderSystem.disableScissor();
            graphics.pose().popPose();
        }
    }
}
