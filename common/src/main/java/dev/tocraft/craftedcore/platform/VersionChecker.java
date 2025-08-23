package dev.tocraft.craftedcore.platform;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.tocraft.craftedcore.CraftedCore;
import dev.tocraft.craftedcore.CraftedCoreConfig;
import dev.tocraft.craftedcore.event.common.PlayerEvents;
import dev.tocraft.craftedcore.util.NetUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.lang.module.ModuleDescriptor.Version;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.channels.UnresolvedAddressException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@SuppressWarnings({"unused", "UnreachableCode"})
public class VersionChecker {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, Version> CACHED_VERSION = new HashMap<>();
    private static final List<String> MOD_LOADER = List.of("fabric", "neoforge", "forge", "quilt");

    private static void sendUpdateMessage(@NotNull ServerPlayer player, @NotNull Component modName, Version newestVersion) {
        CraftedCore.LOGGER.warn(Component.translatable("craftedcore.update", modName.getString(), newestVersion).getString());
        player.sendSystemMessage(Component.literal(Component.translatable("craftedcore.update", modName, newestVersion).getString()).withColor(new Color(255, 255, 0).getRGB()));
    }

    private static List<Version> processVersions(@NotNull List<String> versions) {
        List<String> sortedVersions = new ArrayList<>();
        for (String version : versions) {
            // strip mod loader
            version = stripModLoader(version);
            if (!version.isBlank() && !sortedVersions.contains(version)) {
                sortedVersions.add(version);
            }
        }
        return sortedVersions.stream().map(Version::parse).sorted().toList();
    }

    private static @NotNull String stripModLoader(@NotNull String input) {
        String s = input.toLowerCase();
        for (String loader : MOD_LOADER) {
            if (s.contains(loader + "-")) s = s.replaceAll(loader + "-", "");
            else if (s.contains(loader)) s = s.replaceAll(loader, "");
        }
        if (s.startsWith("v")) s = s.replaceFirst("v", "");
        // check if version only contains dashes
        if (s.replaceAll("-", "").isBlank()) return "";
        else return s;
    }

    public static void registerModrinthChecker(String modid, String slug, Component modName) {
        // notify player about outdated version
        PlayerEvents.PLAYER_JOIN.register(player -> CompletableFuture.runAsync(() -> {
            if (CraftedCoreConfig.INSTANCE != null && CraftedCoreConfig.INSTANCE.enableVersionChecking) {
                // get the actual mod version
                Version localVersion = PlatformData.getModVersion(modid);
                Version newestVersion = localVersion;

                if (!CACHED_VERSION.containsKey(modid)) {
                    List<String> remoteVersions = getVersionsFromModrinth(slug);
                    List<Version> versions = processVersions(remoteVersions);
                    if (!versions.isEmpty()) {
                        newestVersion = versions.getLast();
                    }
                    CACHED_VERSION.put(modid, newestVersion);
                } else {
                    newestVersion = CACHED_VERSION.get(modid);
                }

                if (localVersion != null && newestVersion != null && newestVersion.compareTo(localVersion) > 0) {
                    sendUpdateMessage(player, modName, newestVersion);
                }
            }
        }));
    }

    private static @NotNull List<String> getVersionsFromModrinth(String slug) {
        String url = getModrinthUrl(slug);
        Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        List<String> versions = new ArrayList<>();

        try {
            Map<String, String> header = new HashMap<>();
            header.put("User-Agent", "crafted-core");
            JsonArray jsonArray = NetUtils.getJsonResponse(GSON, header, new URI(url).toURL()).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                versions.add(jsonElement.getAsJsonObject().get("name").getAsString());
            }

        } catch (Exception e) {
            if ((e instanceof UnresolvedAddressException || e instanceof SocketException || e instanceof UnknownHostException)) {
                CraftedCore.reportMissingInternet(e);
            } else {
                CraftedCore.LOGGER.error("Caught an error while getting the newest versions from: {}", url, e);
            }
        }

        return versions;
    }

    private static @NotNull String getModrinthUrl(String slug) {
        String baseUrl = "https://api.modrinth.com/v2/project/" + slug + "/version";
        // filter for compatibility
        PlatformData.ModLoader modLoader = PlatformData.getModLoaderId();
        String loaders = modLoader != PlatformData.ModLoader.OTHER ? "?loaders=[\"" + modLoader.name().toLowerCase() + "\"]" : "";
        Version mcVersion = PlatformData.getModVersion("minecraft");
        String params = loaders + (mcVersion != null ? "&game_versions=[\"" + mcVersion.toString() + "\"]" : "");
        return (baseUrl + params).replace("\"", "%22"); // fix invalid chars
    }
}
