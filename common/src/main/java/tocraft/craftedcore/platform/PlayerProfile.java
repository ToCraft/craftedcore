package tocraft.craftedcore.platform;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.gui.TextureCache;
import tocraft.craftedcore.util.NetUtils;

import java.io.IOException;
import java.net.*;
import java.nio.channels.UnresolvedAddressException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public record PlayerProfile(@NotNull String name, @NotNull UUID id, @Nullable URL skin, boolean isSlim, @Nullable URL cape) {
    private static final Map<String, UUID> NAME_TO_UUID_CACHE = new ConcurrentHashMap<>();
    private static final Map<UUID, PlayerProfile> UUID_TO_PROFILE_CACHE = new ConcurrentHashMap<>();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Nullable
    public static PlayerProfile getCachedProfile(UUID id) {
        return UUID_TO_PROFILE_CACHE.get(id);
    }

    @Nullable
    public static UUID getCachedId(String name) {
        return NAME_TO_UUID_CACHE.get(name);
    }

    @Nullable
    public static PlayerProfile getCachedProfile(String name) {
        UUID id = getCachedId(name);
        if (id != null) {
            return UUID_TO_PROFILE_CACHE.get(id);
        } else {
            return null;
        }
    }

    @Nullable
    public static UUID getUUID(@NotNull final String name) {
        return NAME_TO_UUID_CACHE.computeIfAbsent(name, key -> {
            JsonObject lookup;
            try {
                JsonElement response = NetUtils.getJsonResponse(GSON, new URI("https://api.mojang.com/users/profiles/minecraft/" + key).toURL());
                if (response == null) {
                    return null;
                }
                lookup = response.getAsJsonObject();
                return stringToUUID(lookup.get("id").getAsString());
            } catch (URISyntaxException | IOException | UnresolvedAddressException e) {
                if ((e instanceof UnresolvedAddressException || e instanceof SocketException || e instanceof UnknownHostException)) {
                    CraftedCore.reportMissingInternet(e);
                } else {
                    CraftedCore.LOGGER.error("Caught an exception.", e);
                }
                return null;
            }
        });
    }

    @Nullable
    public static PlayerProfile ofName(@NotNull final String name) {
        UUID uuid = getUUID(name);
        return uuid != null ? ofId(uuid) : null;
    }

    @Nullable
    public static PlayerProfile ofId(@NotNull final UUID uuid) {
        return UUID_TO_PROFILE_CACHE.computeIfAbsent(uuid, key -> {
            JsonObject profile;
            try {
                JsonElement response = NetUtils.getJsonResponse(GSON, new URI("https://sessionserver.mojang.com/session/minecraft/profile/" + key.toString().replace("-", "")).toURL());
                if (response == null) {
                    return null;
                }
                profile = response.getAsJsonObject();
            } catch (URISyntaxException | IOException | UnresolvedAddressException e) {
                CraftedCore.LOGGER.error("Caught an exception.", e);
                return null;
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

            return new PlayerProfile(name, key, skin, isSlim, cape);
        });
    }

    private static UUID stringToUUID(final String input) throws IllegalArgumentException {
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            return UUID.fromString(input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
        }
    }

    public ResourceLocation getSkinId() {
        if (skin != null) {
            return TextureCache.getTextureId(CraftedCore.MODID, "entity", "custom_skin_", "png", skin);
        } else {
            return null;
        }
    }

    public ResourceLocation getCapeId() {
        if (cape != null) {
            return TextureCache.getTextureId(CraftedCore.MODID, "entity", "custom_cape_", "png", cape);
        } else {
            return null;
        }
    }
}
