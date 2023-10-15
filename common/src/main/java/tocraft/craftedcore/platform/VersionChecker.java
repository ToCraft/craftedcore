package tocraft.craftedcore.platform;

import tocraft.craftedcore.CraftedCore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class VersionChecker {
	public static String versionURL = "https://raw.githubusercontent.com/ToCraft/craftedcore/1.20.1/gradle.properties";
	public static boolean checkedUpdate = false;

    public static void checkForUpdates(ServerPlayer player) {
		if (!checkedUpdate) {
			try {
				String line;
				URL url = (new URI(versionURL)).toURL();
				BufferedReader updateReader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
				String updateVersion = CraftedCore.getVersion();
				while ((line = updateReader.readLine()) != null) {
					if (line.startsWith("mod_version=")) {
						updateVersion = line.split("mod_version=")[1];
						break;
					}
				}
				updateReader.close();
				if (!updateVersion.equals(CraftedCore.getVersion())) {
					player.sendSystemMessage(Component.translatable("craftedcore.update", updateVersion));
				}
			}
			catch (Exception e) {
				CraftedCore.LOGGER.warn("Version check failed");
				e.printStackTrace();
			}
			checkedUpdate = true;
		}
	}
}