package tocraft.craftedcore.platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.jetbrains.annotations.Nullable;

import tocraft.craftedcore.CraftedCore;

public class VersionChecker {

	@Nullable
	public static String checkForNewVersion(String urlToCheck) {
		try {
			return checkForNewVersion((new URI(urlToCheck).toURL()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
    public static String checkForNewVersion(URL urlToCheck) {
		try {
			String line;
			URL url = urlToCheck;
			BufferedReader updateReader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
			String updateVersion = CraftedCore.getVersion();
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