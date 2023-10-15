package tocraft.craftedcore.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.platform.Platform;

public class ConfigLoader {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static <C extends Config> C read(Class<C> configClass) {
		try {
			Path configFolder = Platform.getConfigPath();
			Path configFile = Paths.get(configFolder.toString(), CraftedCore.MODID + ".json");

			// Write & return a configuration file
			C config = configClass.getDeclaredConstructor().newInstance();
			writeConfigFile(configFile, config);
			return config;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private static <C extends Config>void writeConfigFile(Path file, C config) {
		try {
			if (!Files.exists(file)) {
				Files.createFile(file);
			}

			Files.writeString(file, GSON.toJson(config));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
