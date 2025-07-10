package dev.tocraft.craftedcore.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("unused")
public class TimerOverlayRenderer {
    public static void register(GuiGraphics graphics, int currentCooldown, int maxCooldown, Item item) {
        Minecraft client = Minecraft.getInstance();

        if (client.screen instanceof ChatScreen || currentCooldown <= 0 || item == null) {
            return;
        }

        double d = client.getWindow().getGuiScale();
        float cooldownScale = 1 - currentCooldown / (float) maxCooldown;

        if (client.player != null) {
            int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            graphics.pose().pushMatrix();
            if (cooldownScale != 1) {
                graphics.enableScissor(
                        (int) ((double) 0 * d),
                        (int) ((double) 0 * d),
                        (int) ((double) width * d),
                        (int) ((double) height * (.02 + .055 * cooldownScale) * d)); // min is 0.21, max is 0.76. dif = .55);
            }

            ItemStack stack = new ItemStack(item);
            graphics.renderItem(stack, (int) (width * .95f), (int) (height * .92f));

            if (cooldownScale != 1) {
                graphics.disableScissor();
            }

            graphics.pose().popMatrix();
        }
    }
}
