package tocraft.craftedcore.platform.forge;

import java.nio.file.Path;

import net.minecraftforge.fml.loading.FMLPaths;

public class PlatformImpl {
	
	public static Path getConfigPath() {
		return FMLPaths.CONFIGDIR.get();
	}
}
