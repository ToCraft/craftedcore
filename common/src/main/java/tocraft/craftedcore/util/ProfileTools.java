package tocraft.craftedcore.util;

import com.google.gson.*;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("unused")
public class ProfileTools {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public record PlayerProfile(String name, UUID id, @Nullable URL skin, boolean isSlim, @Nullable URL cape) {
        public static PlayerProfile ofName(String name) throws IOException, IllegalArgumentException, URISyntaxException {
            JsonObject lookup = NetUtils.getJsonResponse(GSON, new URI("https://api.mojang.com/users/profiles/minecraft/" + name).toURL()).getAsJsonObject();
            return ofId(stringToUUID(lookup.get("id").getAsString()));
        }

        public static PlayerProfile ofId(final UUID uuid) throws IOException, IllegalArgumentException, URISyntaxException {
            JsonObject profile = NetUtils.getJsonResponse(GSON, new URI("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "")).toURL()).getAsJsonObject();
            JsonArray properties = profile.get("properties").getAsJsonArray();
            JsonObject textures = null;
            for (JsonElement property : properties) {
                if (property.isJsonObject()) {
                    JsonObject propertyObject = property.getAsJsonObject();
                    if (Objects.equals(propertyObject.get("name").getAsString(), "textures")) {
                        String decodedProperties = new String(Base64.getDecoder().decode(propertyObject.get("value").getAsString()), StandardCharsets.UTF_8);
                        JsonObject jsonProperties = GsonHelper.fromJson(GSON, decodedProperties, JsonElement.class).getAsJsonObject();
                        textures = jsonProperties.get("textures").getAsJsonObject();
                        break;
                    }
                }
            }

            String name = profile.get("name").getAsString();
            URL skin = null;
            boolean isSlim = false;
            URL cape = null;
            if (textures != null) {
                if (textures.has("SKIN")) {
                    JsonObject skinJson = textures.get("SKIN").getAsJsonObject();
                    skin = new URI(skinJson.get("url").getAsString()).toURL();
                    if (skinJson.has("metadata")) {
                        JsonObject metadata = skinJson.get("metadata").getAsJsonObject();
                        if (metadata.has("model")) {
                            isSlim = Objects.equals(metadata.get("model").getAsString(), "slim");
                        }
                    }
                }
                if (textures.has("CAPE")) {
                    cape = new URI(textures.get("CAPE").getAsJsonObject().get("url").getAsString()).toURL();
                }
            }

            return new PlayerProfile(name, uuid, skin, isSlim, cape);
        }
    }

    public static UUID stringToUUID(final String input) throws IllegalArgumentException {
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            return UUID.fromString(input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
        }
    }
}
