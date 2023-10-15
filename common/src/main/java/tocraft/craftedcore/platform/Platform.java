package tocraft.craftedcore.platform;

import java.nio.file.Path;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class Platform {

	@ExpectPlatform
	public static Path getConfigPath() {
		throw new AssertionError();
	}
}
