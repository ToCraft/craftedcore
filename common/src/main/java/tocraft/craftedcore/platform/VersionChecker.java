package tocraft.craftedcore.platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.jetbrains.annotations.Nullable;

import net.minecraft.network.chat.Component;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.events.common.PlayerEvents;

public class VersionChecker {
	
	public static void registerChecker(String modid, String versionURL) {
		registerChecker(modid, versionURL, Component.literal(modid));
	}
	
	public static void registerChecker(String modid, String versionURL, Component modName) {
		PlayerEvents.PLAYER_JOIN.register(player -> {
			// get newest version from Uri
			String newestVersion = VersionChecker.checkForNewVersion(modid, versionURL);
			// Warns in the log, if checking failed
			if (newestVersion == null)
				CraftedCore.LOGGER.warn("Version check for "+ modName.getString() + " failed");
			// Notifies the joined player, that newer version is available
			else if (!newestVersion.equals(Platform.getMod(modid).getVersion())) {
				player.sendSystemMessage(Component.translatable(CraftedCore.MODID + ".update", modName, newestVersion));
			}
		});
	}
	
	@Nullable
	@Deprecated
	public static String checkForNewVersion(String urlToCheck) {
		try {
			return checkForNewVersion(CraftedCore.MODID, (new URI(urlToCheck).toURL()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Nullable
	public static String checkForNewVersion(String modid, String urlToCheck) {
		try {
			return checkForNewVersion(modid, (new URI(urlToCheck).toURL()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Nullable
    public static String checkForNewVersion(String modid, URL urlToCheck) {
		try {
			String line;
			URL url = urlToCheck;
			BufferedReader updateReader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
			String updateVersion = Platform.getMod(modid).getVersion();
			while ((line = updateReader.readLine()) != null) {
				if (line.startsWith("mod_version=")) {
					updateVersion = line.split("mod_version=")[1];
					break;
				}
			}
			updateReader.close();
			return updateVersion;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}