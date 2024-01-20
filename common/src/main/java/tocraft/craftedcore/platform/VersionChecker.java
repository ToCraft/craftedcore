package tocraft.craftedcore.platform;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;
import net.minecraft.network.chat.Component;
import tocraft.craftedcore.CraftedCore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class VersionChecker {

    public static void registerChecker(String modid, URL versionURL, Component modName) {
        PlayerEvent.PLAYER_JOIN.register(player -> {
            // get newest version from Uri
            String newestVersion = Platform.getMod(modid).getVersion();
            try {
                newestVersion = VersionChecker.checkForNewVersion(versionURL, "mod_version=");

                if (newestVersion.isBlank()) {
                    CraftedCore.LOGGER.warn("Failed to get the newest version for " + modName.getString() + " from " + versionURL + ".");
                    return;
                }
            } catch (IOException e) {
                // Warns in the log, if checking failed
                CraftedCore.LOGGER.warn("Failed to get the newest version for " + modName.getString() + " from " + versionURL + ": " + e.getMessage());
            }
            if (!newestVersion.equals(Platform.getMod(modid).getVersion())) {
                player.sendSystemMessage(Component.translatable(CraftedCore.MODID + ".update", modName, newestVersion));
            }
        });
    }

    public static String checkForNewVersion(URL urlToCheck, String linePrefix) throws IOException {
        String line;
        BufferedReader updateReader = new BufferedReader(new InputStreamReader(urlToCheck.openStream(), StandardCharsets.UTF_8));
        while ((line = updateReader.readLine()) != null) {
            if (line.startsWith(linePrefix)) {
                return line.split(linePrefix)[1];
            }
        }
        updateReader.close();
        return "";
    }
}