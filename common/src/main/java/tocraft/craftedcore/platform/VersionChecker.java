package tocraft.craftedcore.platform;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;
import net.minecraft.network.chat.Component;
import tocraft.craftedcore.CraftedCore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.module.ModuleDescriptor;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VersionChecker {
    public static void registerMavenChecker(String modid, URL mavenURL, Component modName) {
        registerChecker(modid, mavenURL, "<version>" + Platform.getMinecraftVersion() + "-", "</version>", modName);
    }

    public static void registerChecker(String modid, URL urlToCheck, String linePrefix, String lineSuffix, Component modName) {
        PlayerEvent.PLAYER_JOIN.register(player -> {
            if (CraftedCore.CONFIG != null && CraftedCore.CONFIG.enableVersionChecking) {
                // get newest version from Uri
                String localVersion = Platform.getMod(modid).getVersion();
                String newestVersion = localVersion;
                try {
                    List<String> versions = checkForNewVersionFromMaven(urlToCheck, linePrefix, lineSuffix, localVersion);
                    if (!versions.isEmpty()) newestVersion = versions.get(versions.size() - 1);
                } catch (IOException e) {
                    // Warns in the log, if checking failed
                    CraftedCore.LOGGER.error("Failed to get the newest version for " + modName.getString() + " from " + urlToCheck + ".", e);
                }

                if (!newestVersion.equals(localVersion)) {
                    player.sendSystemMessage(Component.translatable(CraftedCore.MODID + ".update", modName, newestVersion));
                }
            }
        });
    }

    @Deprecated
    public static String checkForNewVersion(URL urlToCheck, String linePrefix, String lineSuffix) throws IOException {
        List<String> versions = checkForNewVersionFromMaven(urlToCheck, linePrefix, lineSuffix);
        return !versions.isEmpty() ? versions.get(versions.size() - 1) : "";
    }

    public static List<String> checkForNewVersionFromMaven(URL mavenURL, String linePrefix, String lineSuffix, String... localVersions) throws IOException {
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