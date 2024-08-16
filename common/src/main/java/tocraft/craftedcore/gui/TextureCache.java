package tocraft.craftedcore.gui;

import com.mojang.blaze3d.platform.NativeImage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.patched.Identifier;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class TextureCache {
    private static final Map<String, ResourceLocation> TEXTURE_CACHE = new ConcurrentHashMap<>();

    /**
     * Converts a URL to a readable id
     *
     * @param namespace the namespace in which the id should be saved
     * @param type for the id: "textures/entity/skin_123.png", choose: "entity"
     * @param prefix  for the id: "textures/entity/skin_123.png", choose: "skin". (some number will be auto-generated)
     * @param fileType the file type, e.g. "png"
     * @param textureURL the URL where the texture is found
     * @return the id if the texture could be cached or null, if there was an exception
     */
    @Nullable
    public static ResourceLocation getTextureId(String namespace, String type, String prefix, String fileType, URL textureURL) {
        return TEXTURE_CACHE.computeIfAbsent(String.valueOf(textureURL), key -> {
            ResourceLocation id = Identifier.parse(namespace, "textures/" + type + "/" + prefix + key.hashCode() + "." + fileType);
            try(InputStream is = textureURL.openStream()) {
                NativeImage image = NativeImage.read(new ByteArrayInputStream(is.readAllBytes()));
                DynamicTexture dynamicTexture = new DynamicTexture(image);
                Minecraft.getInstance().getTextureManager().register(id, dynamicTexture);
            } catch (IOException e) {
                CraftedCore.LOGGER.error("Caught an exception while reading url: {}", textureURL, e);
                return null;
            }
            return id;
        });
    }
}
