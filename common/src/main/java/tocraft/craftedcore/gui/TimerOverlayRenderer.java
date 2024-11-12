package tocraft.craftedcore.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
//#if MC>1194
import net.minecraft.client.gui.GuiGraphics;
//#else
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//#endif
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("unused")
public class TimerOverlayRenderer {
    //#if MC>1194
    public static void register(GuiGraphics graphics, int currentCooldown, int maxCooldown, Item item) {
    //#else
    //$$ public static void register(PoseStack graphics, int currentCooldown, int maxCooldown, Item item) {
    //#endif
        Minecraft client = Minecraft.getInstance();

        if (client.screen instanceof ChatScreen || currentCooldown <= 0 || item == null) {
            return;
        }

        double d = client.getWindow().getGuiScale();
        float cooldownScale = 1 - currentCooldown / (float) maxCooldown;

        if (client.player != null) {
            int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            //#if MC>1194
            graphics.pose().pushPose();
            //#if MC>=1212
            graphics.flush();
            //#endif
            //#else
            //$$ graphics.pushPose();
            //#endif
            if (cooldownScale != 1) {
                RenderSystem.enableScissor(
                        (int) ((double) 0 * d),
                        (int) ((double) 0 * d),
                        (int) ((double) width * d),
                        (int) ((double) height * (.02 + .055 * cooldownScale) * d)); // min is 0.21, max is 0.76. dif = .55
            }

            ItemStack stack = new ItemStack(item);
            //#if MC>1194
            graphics.renderItem(stack, (int) (width * .95f), (int) (height * .92f));
            //#elseif MC>1182
            //$$ Minecraft.getInstance().getItemRenderer().renderGuiItem(graphics, stack, (int) (width * .95f), (int) (height * .92f));
            //#else
            //$$ Minecraft.getInstance().getItemRenderer().renderGuiItem(stack, (int) (width * .95f), (int) (height * .92f));
            //#endif

            if (cooldownScale != 1) {
                RenderSystem.disableScissor();
            }

            //#if MC>1194
            graphics.pose().popPose();
            //#else
            //$$ graphics.popPose();
            //#endif
        }
    }
}
