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
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.CraftedCoreConfig;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.lang.module.ModuleDescriptor.Version;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VersionChecker {
    private static final Map<String, Version> CACHED_VERSION = new HashMap<>();
    private static final List<String> INVALID_VERSIONS = List.of("1.16.5", "1.18.2", "1.19.4", "1.20.1", "1.20.2", "1.20.4", "1.20.5");

    public static void registerMavenChecker(String modid, URL mavenURL, Component modName) {
        // notify player about outdated version
        PlayerEvent.PLAYER_JOIN.register(player -> new Thread(() -> {
            if (CraftedCoreConfig.INSTANCE != null && CraftedCoreConfig.INSTANCE.enableVersionChecking) {
                // get the actual mod version
                Version localVersion = Version.parse(Platform.getMod(modid).getVersion());
                Version newestVersion = localVersion;

                if (CACHED_VERSION.containsKey(modid)) {
                    newestVersion = CACHED_VERSION.get(modid);
                } else {
                    List<String> remoteVersions = new ArrayList<>();
                    try {
                        // handle XML file
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                        factory.setIgnoringComments(true);
                        factory.setIgnoringElementContentWhitespace(true);
                        factory.setValidating(false);

                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document xmlFile = builder.parse(new InputSource(mavenURL.openStream()));
                        NodeList nodeList = xmlFile.getElementsByTagName("version");
                        for (int i = 0; i < nodeList.getLength(); i++) {
                            remoteVersions.add(nodeList.item(i).getTextContent());
                        }
                    } catch (Exception e) {
                        CraftedCore.LOGGER.error("Caught an error while getting the newest versions from: " + mavenURL, e);
                    }
                    List<Version> versions = new ArrayList<>(processVersionListWithDefaultLayout(remoteVersions, true, INVALID_VERSIONS));
                    if (!versions.isEmpty()) {
                        newestVersion = versions.get(versions.size() - 1);
                    }
                    CACHED_VERSION.put(modid, newestVersion);
                }
                if (newestVersion.compareTo(localVersion) > 0) {
                    CraftedCore.LOGGER.warn(new TranslatableComponent(CraftedCore.MODID + ".update", modName, newestVersion).getString());
                    player.displayClientMessage(new TranslatableComponent(CraftedCore.MODID + ".update", modName, newestVersion), false);
                }
            }
        }, VersionChecker.class.getSimpleName()).start());
    }

    public static void registerDefaultGitHubChecker(String modid, String owner, String repo, Component modName) {
        registerGitHubChecker(modid, owner, repo, false, true, modName, INVALID_VERSIONS);
    }


    public static void registerGitHubChecker(String modid, String owner, String repo, boolean releasesInsteadOfTags, boolean useLastPartOfVersion, Component modName, List<String> invalidVersions) {
        // notify player about outdated version
        PlayerEvent.PLAYER_JOIN.register(player -> new Thread(() -> {
            if (CraftedCoreConfig.INSTANCE != null && CraftedCoreConfig.INSTANCE.enableVersionChecking) {
                // get the actual mod version
                Version localVersion = Version.parse(Platform.getMod(modid).getVersion());
                Version newestVersion = localVersion;

                if (!CACHED_VERSION.containsKey(modid)) {
                    List<String> remoteVersions = getVersionsFromGitHub(owner, repo, releasesInsteadOfTags);
                    List<Version> versions = new ArrayList<>(processVersionListWithDefaultLayout(remoteVersions, useLastPartOfVersion, invalidVersions));
                    if (!versions.isEmpty()) {
                        newestVersion = versions.get(versions.size() - 1);
                    }
                    CACHED_VERSION.put(modid, newestVersion);
                } else {
                    newestVersion = CACHED_VERSION.get(modid);
                }

                if (newestVersion.compareTo(localVersion) > 0) {
                    CraftedCore.LOGGER.warn(new TranslatableComponent(CraftedCore.MODID + ".update", modName, newestVersion).getString());
                    player.displayClientMessage(new TranslatableComponent(CraftedCore.MODID + ".update", modName, newestVersion), false);
                }
            }
        }, VersionChecker.class.getSimpleName()).start());
    }

    private static List<Version> processVersionListWithDefaultLayout(List<String> versions, boolean useLast, List<String> invalidVersions) {
        List<String> sortedVersions = new ArrayList<>();
        for (String version : versions) {
            if (version.contains("-")) {
                String processedVersion = version.split("-")[useLast ? 1 : 0];
                if (!sortedVersions.contains(processedVersion) && !invalidVersions.contains(processedVersion)) {
                    sortedVersions.add(processedVersion);
                }
            }
        }
        return sortedVersions.stream().map(Version::parse).sorted().toList();
    }

    private static List<String> getVersionsFromGitHub(String owner, String repo, boolean releasesInsteadOfTags) {
        String url = "https://api.github.com/repos/" + owner + "/" + repo + (releasesInsteadOfTags ? "/releases" : "/tags");
        Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        List<String> versions = new ArrayList<>();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(url);
            request.addHeader("Accept", "application/vnd.github.v3+json");
            CloseableHttpResponse result = httpClient.execute(request);
            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
            JsonArray jsonArray = GsonHelper.fromJson(GSON, json, JsonArray.class);
            for (JsonElement jsonElement : jsonArray) {
                versions.add(jsonElement.getAsJsonObject().get("name").getAsString());
            }

        } catch (Exception e) {
            CraftedCore.LOGGER.error("Caught an error while getting the newest " + (releasesInsteadOfTags ? "releases" : "tags") + " from " + url, e);
        }

        return versions;
    }
}