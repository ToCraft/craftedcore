package tocraft.craftedcore.platform.fabric;

import java.nio.file.Path;

import net.fabricmc.loader.api.FabricLoader;

public class PlatformImpl {

	public static Path getConfigPath() {
		return FabricLoader.getInstance().getConfigDir();
	}
}
