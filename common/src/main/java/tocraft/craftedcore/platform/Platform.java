package tocraft.craftedcore.platform;

import java.nio.file.Path;
import java.util.Collection;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class Platform {

	@ExpectPlatform
	public static Path getConfigPath() {
		throw new AssertionError();
	}
	
    @ExpectPlatform
    public static boolean isModLoaded(String id) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static Mod getMod(String id) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static Collection<Mod> getMods() {
        throw new AssertionError();
    }
}
