package tocraft.craftedcore.patched.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

//#if MC>1194
import net.minecraft.client.gui.GuiGraphics;
//#else
//$$ import com.mojang.blaze3d.systems.RenderSystem;
//$$ import net.minecraft.client.gui.GuiComponent;
//$$ import net.minecraft.client.renderer.GameRenderer;
//$$ import net.minecraft.util.FastColor;
//$$ import com.mojang.blaze3d.vertex.*;
//#if MC>1182
//$$ import org.joml.Matrix4f;
//#else
//$$ import com.mojang.math.Matrix4f;
//#endif
//#endif

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class CGraphics {
    //#if MC>1194
    public static void blit(GuiGraphics graphics, ResourceLocation texture, int i, int j, int k, int l, float f, float g, int m, int n, int o, int p) {
        graphics.blit(texture, i, j, k, l, f, g, m, n, o, p);
    }
    
    public static void blit(GuiGraphics graphics, int i, int j, int k, int l, int m, TextureAtlasSprite textureAtlasSprite) {
        graphics.blit(i, j, k, l, m, textureAtlasSprite);
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
    
    //#else
    //$$ public static void blit(PoseStack graphics, ResourceLocation texture, int i, int j, int k, int l, float f, float g, int m, int n, int o, int p) {
    //$$     RenderSystem.setShaderTexture(0, texture);
    //$$     GuiComponent.blit(graphics, i, j, k, l, f, g, m, n, o, p);
    //$$ }
    //$$
    //$$ public static void fillTransparent(PoseStack graphics, int x1, int y1, int x2, int y2) {
    //$$     fillGradient(graphics, x1, y1, x2, y2, -1072689136, -804253680);
    //$$ }
    //$$
    //$$ public static void fillGradient(PoseStack poseStack, int x1, int y1, int x2, int y2, int colorFrom, int colorTo) {
    //$$     fillGradient(poseStack, x1, y1, x2, y2, colorFrom, colorTo, 0);
    //$$ }
    //$$
    //$$ @SuppressWarnings("SameParameterValue")
    //$$ private static void fillGradient(PoseStack poseStack, int x1, int y1, int x2, int y2, int colorFrom, int colorTo, int blitOffset) {
    //$$     RenderSystem.enableBlend();
    //$$     RenderSystem.setShader(GameRenderer::getPositionColorShader);
    //$$     Tesselator tesselator = Tesselator.getInstance();
    //$$     BufferBuilder bufferBuilder = tesselator.getBuilder();
    //$$     bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
    //$$     fillGradient(poseStack.last().pose(), bufferBuilder, x1, y1, x2, y2, blitOffset, colorFrom, colorTo);
    //$$     tesselator.end();
    //$$     RenderSystem.disableBlend();
    //$$ }
    //$$
    //$$ private static void fillGradient(Matrix4f matrix, BufferBuilder builder, int x1, int y1, int x2, int y2, int blitOffset, int colorA, int colorB) {
    //$$     float f = (float) FastColor.ARGB32.alpha(colorA) / 255.0F;
    //$$     float g = (float) FastColor.ARGB32.red(colorA) / 255.0F;
    //$$     float h = (float) FastColor.ARGB32.green(colorA) / 255.0F;
    //$$     float i = (float) FastColor.ARGB32.blue(colorA) / 255.0F;
    //$$     float j = (float) FastColor.ARGB32.alpha(colorB) / 255.0F;
    //$$     float k = (float) FastColor.ARGB32.red(colorB) / 255.0F;
    //$$     float l = (float) FastColor.ARGB32.green(colorB) / 255.0F;
    //$$     float m = (float) FastColor.ARGB32.blue(colorB) / 255.0F;
    //$$     builder.vertex(matrix, (float) x1, (float) y1, (float) blitOffset).color(g, h, i, f).endVertex();
    //$$     builder.vertex(matrix, (float) x1, (float) y2, (float) blitOffset).color(k, l, m, j).endVertex();
    //$$     builder.vertex(matrix, (float) x2, (float) y2, (float) blitOffset).color(k, l, m, j).endVertex();
    //$$     builder.vertex(matrix, (float) x2, (float) y1, (float) blitOffset).color(g, h, i, f).endVertex();
    //$$ }
    //$$
    //$$     public static void drawString(PoseStack context, Component text, int i, int j, int k, boolean bl) {
    //$$         Minecraft.getInstance().font.drawShadow(context, text.getString(), i, j, k, bl);
    //$$     }
    //$$
    //$$     public static void blit(PoseStack graphics, int i, int j, int k, int l, int m, TextureAtlasSprite textureAtlasSprite) {
    //$$         GuiComponent.blit(graphics, i, j, k, l, m, textureAtlasSprite);
    //$$     }
    //#endif
}
