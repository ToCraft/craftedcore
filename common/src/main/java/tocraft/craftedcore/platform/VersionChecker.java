package tocraft.craftedcore.platform;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
        return Arrays.asList(sortVersions(versions.toArray(String[]::new)));
    }

    /**
     * Sorts the specified versions
     *
     * @param versions the versions to be sorted
     * @return {@link java.lang.String String[]} containing the versions, sorted low to high / old to new
     */
    public static String[] sortVersions(String... versions) {
        return Arrays.stream(versions).map(ModuleDescriptor.Version::parse).sorted().map(ModuleDescriptor.Version::toString).toArray(String[]::new);
    }
}