package tocraft.craftedcore.platform;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;
import net.minecraft.network.chat.Component;
import tocraft.craftedcore.CraftedCore;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;


public class VersionChecker {

    @Deprecated
    public static void registerChecker(String modid, String versionURL) {
        registerChecker(modid, versionURL, Component.literal(modid));
    }

    @Deprecated
    public static void registerChecker(String modid, String versionURL, Component modName) {
        try {
            registerChecker(modid, new URI(versionURL).toURL(), modName);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException ignored) {
        }
    }

    public static void registerChecker(String modid, URL versionURL, Component modName) {
        PlayerEvent.PLAYER_JOIN.register(player -> {
            // get newest version from Uri
            String newestVersion = VersionChecker.checkForNewVersion(modid, versionURL);
            // Warns in the log, if checking failed
            if (newestVersion == null)
                CraftedCore.LOGGER.warn("Version check for " + modName.getString() + " failed");
                // Notifies the joined player, that newer version is available
            else if (!newestVersion.equals(Platform.getMod(modid).getVersion())) {
                player.sendSystemMessage(Component.translatable(CraftedCore.MODID + ".update", modName, newestVersion));
            }
        });
    }

    @Deprecated
    public static String checkForNewVersion(String modid, String urlToCheck) {
        try {
            return checkForNewVersion(modid, (new URI(urlToCheck).toURL()));
        } catch (URISyntaxException | MalformedURLException e) {
            return Platform.getMod(modid).getVersion();
        }
    }

    public static String checkForNewVersion(String modid, URL urlToCheck) {
        String version = Platform.getMod(modid).getVersion();

        // try to load from maven properties first
        Properties p = new Properties();

        if (urlToCheck != null) {
            try {
                p.load(urlToCheck.openStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            version = p.getProperty("version", "");
        }

        return version;
    }
}