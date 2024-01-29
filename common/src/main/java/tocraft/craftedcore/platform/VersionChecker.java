package tocraft.craftedcore.platform;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import tocraft.craftedcore.CraftedCore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class VersionChecker {

    public static void registerMavenChecker(String modid, URL mavenURL, Component modName) {
        registerChecker(modid, mavenURL, "<version>" + Platform.getMinecraftVersion() + "-", "</version>", modName);
    }

    public static void registerChecker(String modid, URL urlToCheck, String linePrefix, String lineSuffix, Component modName) {
        PlayerEvent.PLAYER_JOIN.register(player -> {
            if (CraftedCore.CONFIG != null && CraftedCore.CONFIG.enableVersionChecking) {
                // get newest version from Uri
                String newestVersion = Platform.getMod(modid).getVersion();
                try {
                    newestVersion = VersionChecker.checkForNewVersion(urlToCheck, linePrefix, lineSuffix);

                    if (newestVersion.isBlank()) {
                        CraftedCore.LOGGER.warn("Failed to get the newest version for " + modName.getString() + " from " + urlToCheck + ".");
                        return;
                    }
                } catch (IOException e) {
                    // Warns in the log, if checking failed
                    CraftedCore.LOGGER.warn("Failed to get the newest version for " + modName.getString() + " from " + urlToCheck + ": " + e.getMessage());
                }
                if (!newestVersion.equals(Platform.getMod(modid).getVersion())) {
                    player.displayClientMessage(new TranslatableComponent(CraftedCore.MODID + ".update", modName, newestVersion), false);
                }
            }
        });
    }

    public static String checkForNewVersion(URL urlToCheck, String linePrefix, String lineSuffix) throws IOException {
        String line;
        String latestValue = "";
        BufferedReader updateReader = new BufferedReader(new InputStreamReader(urlToCheck.openStream(), StandardCharsets.UTF_8));
        while ((line = updateReader.readLine()) != null) {
            line = line.replaceAll(" ", "");
            if (line.startsWith(linePrefix) && line.endsWith(lineSuffix)) {
                latestValue = line.split(linePrefix)[1].split(lineSuffix)[0];
            }
        }
        updateReader.close();

        return latestValue;

    }
}