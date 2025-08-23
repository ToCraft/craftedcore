package dev.tocraft.craftedcore.platform;

import com.google.gson.*;
import dev.tocraft.craftedcore.CraftedCore;
import dev.tocraft.craftedcore.CraftedCoreConfig;
import dev.tocraft.craftedcore.gui.TextureCache;
import dev.tocraft.craftedcore.util.NetUtils;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.channels.UnresolvedAddressException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public record PlayerProfile(@NotNull String name, @NotNull UUID id, @Nullable URL skin, boolean isSlim,
                            @Nullable URL cape) {
    private static final Map<String, UUID> NAME_TO_UUID_CACHE = new ConcurrentHashMap<>();
    private static final Map<UUID, PlayerProfile> UUID_TO_PROFILE_CACHE = new ConcurrentHashMap<>();
    private final static Path CACHE_DIR = CraftedCore.CACHE_DIR.resolve("player_profiles");
    private final static Path CACHE_PROFILES_DIR = CACHE_DIR.resolve("profiles");
    private final static Path CACHE_SKINS_DIR = CACHE_DIR.resolve("skins");
    private final static Path CACHE_CAPES_DIR = CACHE_DIR.resolve("capes");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @SuppressWarnings({"ResultOfMethodCallIgnored", "SpellCheckingInspection"})
    @ApiStatus.Internal
    public static void mkdirs() {
        CACHE_DIR.toFile().mkdirs();
        CACHE_PROFILES_DIR.toFile().mkdirs();
        CACHE_SKINS_DIR.toFile().mkdirs();
        CACHE_CAPES_DIR.toFile().mkdirs();
    }

    @ApiStatus.Internal
    public static void initialize() {
        try {
            loadAll();
        } catch (IOException e) {
            CraftedCore.LOGGER.error("Caught an exception.", e);
        }
    }

    @ApiStatus.Internal
    public static void clearCache() {
        NAME_TO_UUID_CACHE.clear();
        UUID_TO_PROFILE_CACHE.clear();
        CraftedCore.forceDeleteFile(CACHE_DIR.toFile());
    }

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
        if (CraftedCoreConfig.INSTANCE.autoUpdateCache) {
            UUID cached = NAME_TO_UUID_CACHE.get(name);
            if (cached != null) {
                // update asynchronously
                CompletableFuture.runAsync(() -> _getUUID(name));
                return cached;
            }
        }

        return NAME_TO_UUID_CACHE.computeIfAbsent(name, key -> _getUUID(name));
    }

    @ApiStatus.Internal
    @Nullable
    private static UUID _getUUID(@NotNull final String name) {
        JsonObject lookup;
        try {
            JsonElement response = NetUtils.getJsonResponse(GSON, new URI("https://api.mojang.com/users/profiles/minecraft/" + name).toURL());
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
    }

    @Nullable
    public static PlayerProfile ofName(@NotNull final String name) {
        UUID uuid = getUUID(name);
        return uuid != null ? ofId(uuid) : null;
    }

    @Nullable
    public static PlayerProfile ofId(@NotNull final UUID uuid) {
        if (CraftedCoreConfig.INSTANCE.autoUpdateCache) {
            PlayerProfile cached = UUID_TO_PROFILE_CACHE.get(uuid);
            if (cached != null) {
                // update asynchronously
                CompletableFuture.runAsync(() -> _ofId(uuid));
                return cached;
            }
        }

        return UUID_TO_PROFILE_CACHE.computeIfAbsent(uuid, key -> _ofId(uuid));
    }

    @ApiStatus.Internal
    @Nullable
    private static PlayerProfile _ofId(@NotNull final UUID uuid) {
        JsonObject profile;
        try {
            JsonElement response = NetUtils.getJsonResponse(GSON, new URI("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "")).toURL());
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
                    CraftedCore.LOGGER.error("Caught an exception.", e);
                    return null;
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
                    CraftedCore.LOGGER.error("Caught an exception.", e);
                    return null;
                }
            }
        }

        PlayerProfile playerProfile = new PlayerProfile(name, uuid, skin, isSlim, cape);
        try {
            playerProfile.save();
        } catch (IOException e) {
            CraftedCore.LOGGER.error("Caught an exception.", e);
        }
        return playerProfile;
    }

    private static @NotNull UUID stringToUUID(final String input) throws IllegalArgumentException {
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            return UUID.fromString(input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
        }
    }

    @Nullable
    public ResourceLocation getSkinId() {
        if (skin != null) {
            return TextureCache.getTextureId(CraftedCore.MODID, "entity", "custom_skin_", "png", skin);
        } else {
            return null;
        }
    }

    @Nullable
    public ResourceLocation getCapeId() {
        if (cape != null) {
            return TextureCache.getTextureId(CraftedCore.MODID, "entity", "custom_cape_", "png", cape);
        } else {
            return null;
        }
    }

    @ApiStatus.Internal
    public JsonObject toJson() {
        return GSON.toJsonTree(this).getAsJsonObject();
    }

    @ApiStatus.Internal
    public static PlayerProfile ofJson(JsonObject json) {
        return GSON.fromJson(json, PlayerProfile.class);
    }

    @ApiStatus.Internal
    public void save() throws IOException {
        mkdirs();
        JsonObject json = this.toJson();
        if (skin != null) {
            Path cachedSkin = CACHE_SKINS_DIR.resolve(this.id + ".png");
            try (InputStream is = skin.openStream()) {
                Files.write(cachedSkin, is.readAllBytes());
            }
            json.addProperty("skin", cachedSkin.toFile().toURI().toURL().toString());
        }
        if (cape != null) {
            Path cachedCape = CACHE_CAPES_DIR.resolve(this.id + ".png");
            try (InputStream is = cape.openStream()) {
                Files.write(cachedCape, is.readAllBytes());
            }
            json.addProperty("cape", cachedCape.toFile().toURI().toURL().toString());
        }
        Files.writeString(CACHE_PROFILES_DIR.resolve(this.id() + ".json"), GSON.toJson(json));
    }

    @ApiStatus.Internal
    public static PlayerProfile load(UUID id) throws IOException {
        mkdirs();
        String s = Files.readString(CACHE_PROFILES_DIR.resolve(id + ".json"));
        JsonObject json = GSON.fromJson(s, JsonObject.class);
        return ofJson(json);
    }

    public static void loadAll() throws IOException {
        mkdirs();
        try (Stream<Path> stream = Files.list(CACHE_PROFILES_DIR)) {
            for (Path path : stream.toArray(Path[]::new)) {
                if (path.toString().endsWith(".json")) {
                    String s = Files.readString(path);
                    JsonObject json = GSON.fromJson(s, JsonObject.class);
                    PlayerProfile profile = ofJson(json);
                    NAME_TO_UUID_CACHE.put(profile.name, profile.id);
                    UUID_TO_PROFILE_CACHE.put(profile.id, profile);
                } else {
                    System.out.println(path);
                }
            }
        }
    }
}
