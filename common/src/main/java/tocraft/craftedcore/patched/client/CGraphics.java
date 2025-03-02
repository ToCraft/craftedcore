package tocraft.craftedcore.patched.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class CGraphics {
    public static void blit(GuiGraphics graphics, ResourceLocation atlasLocation, int x, int y, int width, int height, int uOffset, int vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        graphics.blit(RenderType::guiTextured, atlasLocation, x, y, width, height, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
    }

    public static void blit(GuiGraphics graphics, int x, int y, int blitOffset, int width, int height, TextureAtlasSprite sprite) {
        graphics.blitSprite(RenderType::guiTextured, sprite, x, y, blitOffset, width, height);
    }

    public static void fillTransparent(GuiGraphics graphics, int x1, int y1, int x2, int y2) {
        fillGradient(graphics, x1, y1, x2, y2, -1072689136, -804253680);
    }

    public static void fillGradient(GuiGraphics graphics, int x1, int y1, int x2, int y2, int colorFrom, int colorTo) {
        graphics.fillGradient(x1, y1, x2, y2, colorFrom, colorTo);
    }

    public static void drawString(GuiGraphics context, Component text, int i, int j, int k, boolean bl) {
        context.drawString(Minecraft.getInstance().font, text, i, j, k, bl);
    }
}
