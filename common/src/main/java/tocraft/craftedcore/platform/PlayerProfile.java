package tocraft.craftedcore.platform;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tocraft.craftedcore.util.MalformedUUIDException;
import tocraft.craftedcore.util.NetUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SuppressWarnings("unused")
public record PlayerProfile(@NotNull String name, @NotNull UUID id, @Nullable URL skin, boolean isSlim, @Nullable URL cape) {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Nullable
    public static PlayerProfile ofName(@NotNull final String name) throws MalformedUUIDException {
        JsonObject lookup;
        try {
            JsonElement response = NetUtils.getJsonResponse(GSON, new URI("https://api.mojang.com/users/profiles/minecraft/" + name).toURL());
            if (response == null) {
                return null;
            }
            lookup = response.getAsJsonObject();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        try {
            return ofId(stringToUUID(lookup.get("id").getAsString()));
        } catch (IllegalArgumentException e) {
            throw new MalformedUUIDException(e.getMessage());
        }
    }

    @Nullable
    public static PlayerProfile ofId(@NotNull final UUID uuid) {
        JsonObject profile;
        try {
            JsonElement response = NetUtils.getJsonResponse(GSON, new URI("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "")).toURL());
            if (response == null) {
                return null;
            }
            profile = response.getAsJsonObject();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        JsonArray properties = profile.get("properties").getAsJsonArray();
        JsonObject textures = null;
        for (JsonElement property : properties) {
            if (property.isJsonObject()) {
                JsonObject propertyObject = property.getAsJsonObject();
                if (Objects.equals(propertyObject.get("name").getAsString(), "textures")) {
                    String decodedProperties = new String(Base64.getDecoder().decode(propertyObject.get("value").getAsString()), StandardCharsets.UTF_8);
                    JsonObject jsonProperties = GSON.fromJson(decodedProperties, JsonElement.class).getAsJsonObject();
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
                try {
                    skin = new URI(skinJson.get("url").getAsString()).toURL();
                } catch (MalformedURLException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
                if (skinJson.has("metadata")) {
                    JsonObject metadata = skinJson.get("metadata").getAsJsonObject();
                    if (metadata.has("model")) {
                        isSlim = Objects.equals(metadata.get("model").getAsString(), "slim");
                    }
                }
            }
            if (textures.has("CAPE")) {
                try {
                    cape = new URI(textures.get("CAPE").getAsJsonObject().get("url").getAsString()).toURL();
                } catch (MalformedURLException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return new PlayerProfile(name, uuid, skin, isSlim, cape);
    }

    private static UUID stringToUUID(final String input) throws IllegalArgumentException {
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            return UUID.fromString(input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
        }
    }
}