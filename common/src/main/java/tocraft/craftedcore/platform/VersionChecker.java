package tocraft.craftedcore.platform;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.GsonHelper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.CraftedCoreConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.module.ModuleDescriptor;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class VersionChecker {
    private static final Map<String, String> CACHED_VERSION = new HashMap<>();

    public static void registerMavenChecker(String modid, URL mavenURL, Component modName) {
        registerChecker(modid, mavenURL, "<version>" + Platform.getMinecraftVersion() + "-", "</version>", modName);
    }

    public static void registerDefaultGitHubChecker(String modid, String owner, String repo, Component modName) {
        registerGitHubChecker(modid, owner, repo, false, true, modName, "1.16.5", "1.18.2", "1.19.4", "1.20.1", "1.20.2");
    }


    public static void registerGitHubChecker(String modid, String owner, String repo, boolean releasesInsteadOfTags, boolean useLastPartOfVersion, Component modName, String... invalidVersions) {
        // notify player about outdated version
        PlayerEvent.PLAYER_JOIN.register(player -> new Thread(() -> {
            if (CraftedCoreConfig.INSTANCE != null && CraftedCoreConfig.INSTANCE.enableVersionChecking) {
                // get the actual mod version
                String localVersion = Platform.getMod(modid).getVersion();
                String newestVersion = localVersion;

                if (!CACHED_VERSION.containsKey(modid)) {
                    List<String> remoteVersions = getVersionsFromGitHub(owner, repo, releasesInsteadOfTags);
                    remoteVersions.add(localVersion);
                    List<String> versions = new ArrayList<>(processVersionListWithDefaultLayout(remoteVersions, useLastPartOfVersion, invalidVersions));
                    if (!versions.isEmpty()) {
                        newestVersion = versions.get(versions.size() - 1);
                    }
                    CACHED_VERSION.put(modid, newestVersion);
                } else {
                    newestVersion = CACHED_VERSION.get(modid);
                }

                if (!localVersion.equals(newestVersion)) {
                    CraftedCore.LOGGER.warn(new TranslatableComponent(CraftedCore.MODID + ".update", modName, newestVersion).getString());
                    player.displayClientMessage(new TranslatableComponent(CraftedCore.MODID + ".update", modName, newestVersion), false);
                }
            }
        }, VersionChecker.class.getSimpleName()).start());
    }

    private static List<String> processVersionListWithDefaultLayout(List<String> versions, boolean useLast, String... invalidVersions) {
        List<String> sortedVersions = new ArrayList<>();
        List<String> invalidVersionsList = List.of(invalidVersions);
        for (String version : versions) {
            if (version.contains("-")) {
                String processedVersion = version.split("-")[useLast ? 1 : 0];
                if (!sortedVersions.contains(processedVersion) && !invalidVersionsList.contains(processedVersion)) {
                    sortedVersions.add(processedVersion);
                }
            }
        }
        return sortVersions(sortedVersions);
    }

    private static List<String> getVersionsFromGitHub(String owner, String repo, boolean releasesInsteadOfTags) {
        String url = "https://api.github.com/repos/" + owner + "/" + repo + (releasesInsteadOfTags ? "/releases" : "/tags");
        Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        List<String> versions = new ArrayList<>();

        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            request.addHeader("Accept", "application/vnd.github.v3+json");
            HttpResponse result = httpClient.execute(request);
            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
            JsonArray jsonArray = GsonHelper.fromJson(GSON, json, JsonArray.class);
            for (JsonElement jsonElement : jsonArray) {
                versions.add(jsonElement.getAsJsonObject().get("name").getAsString());
            }

        } catch (IOException e) {
            CraftedCore.LOGGER.error("Caught an error while getting the newest " + (releasesInsteadOfTags ? "releases" : "tags") + " from " + url, e);
        }

        return versions;
    }

    public static void registerChecker(String modid, URL urlToCheck, String linePrefix, String lineSuffix, Component modName) {
        // cache versions in extra thread to long loading times
        new Thread(() -> cacheNewestVersion(modid, urlToCheck, linePrefix, lineSuffix)).start();

        // notify player about outdated version
        PlayerEvent.PLAYER_JOIN.register(player -> new Thread(() -> {
            if (CraftedCoreConfig.INSTANCE != null && CraftedCoreConfig.INSTANCE.enableVersionChecking) {
                // get the actual mod version
                String localVersion = Platform.getMod(modid).getVersion();
                String newestVersion = cacheNewestVersion(modid, urlToCheck, linePrefix, lineSuffix);

                if (!localVersion.equals(newestVersion)) {
                    player.displayClientMessage(new TranslatableComponent(CraftedCore.MODID + ".update", modName, newestVersion), false);
                }
            }
        }).start());
    }

    public static String cacheNewestVersion(String modid, URL urlToCheck, String linePrefix, String lineSuffix) {
        if (CACHED_VERSION.get(modid) == null) {
            // get the actual mod version
            String localVersion = Platform.getMod(modid).getVersion();
            String newestVersion = localVersion;
            // get newest version from URL
            try {
                List<String> versions = checkForNewVersionFromURL(urlToCheck, linePrefix, lineSuffix, localVersion);
                if (!versions.isEmpty()) newestVersion = versions.get(versions.size() - 1);
            } catch (IOException e) {
                // Warns in the log, if checking failed
                CraftedCore.LOGGER.error("Failed to get the newest version for " + modid + " from " + urlToCheck + ".", e);
            }

            CACHED_VERSION.put(modid, newestVersion);
            return newestVersion;
        } else return CACHED_VERSION.get(modid);
    }

    @Deprecated
    public static String checkForNewVersion(URL urlToCheck, String linePrefix, String lineSuffix) throws IOException {
        List<String> versions = checkForNewVersionFromURL(urlToCheck, linePrefix, lineSuffix);
        return !versions.isEmpty() ? versions.get(versions.size() - 1) : "";
    }

    public static List<String> checkForNewVersionFromURL(URL mavenURL, String linePrefix, String lineSuffix, String... localVersions) throws IOException {
        String line;
        List<String> versions = new ArrayList<>();
        BufferedReader updateReader = new BufferedReader(new InputStreamReader(mavenURL.openStream(), StandardCharsets.UTF_8));
        while ((line = updateReader.readLine()) != null) {
            line = line.replaceAll(" ", "").replaceAll("\n", "").replaceAll("\t", "");
            if (line.startsWith(linePrefix) && line.endsWith(lineSuffix)) {
                versions.add(line.split(linePrefix)[1].split(lineSuffix)[0]);
            }
        }
        updateReader.close();
        versions.addAll(Arrays.asList(localVersions));
        return sortVersions(versions);
    }

    /**
     * Sorts the specified versions
     *
     * @param versions the versions to be sorted
     * @return {@link List<String>} containing the versions, sorted low to high / old to new
     */
    public static List<String> sortVersions(List<String> versions) {
        return versions.stream().map(ModuleDescriptor.Version::parse).sorted().map(ModuleDescriptor.Version::toString).toList();
    }
}