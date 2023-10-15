package tocraft.craftedcore.platform.fabric;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import tocraft.craftedcore.platform.Mod;

public class PlatformImpl {

	public static Path getConfigPath() {
		return FabricLoader.getInstance().getConfigDir();
	}
	
	public static boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }
    
    public static Mod getMod(String id) {
        return new ModImpl(id);
        
    }
    
    public static Collection<Mod> getMods() {
    	Collection<Mod> mods = new HashSet<Mod>();
    	FabricLoader.getInstance().getAllMods().forEach(mod -> {
    		mods.add(getMod(mod.getMetadata().getId()));
    	});
    	
    	return mods;
    }
    
    private static class ModImpl implements Mod {
        private final ModContainer container;
        private final ModMetadata metadata;
        
		public ModImpl(String id) {
            this.container = FabricLoader.getInstance().getModContainer(id).orElseThrow();
            this.metadata = this.container.getMetadata();
        }
        
        @Override
        public String getModId() {
            return metadata.getId();
        }
        
        @Override
        public String getVersion() {
            return metadata.getVersion().getFriendlyString();
        }
        
        @Override
        public String getName() {
            return metadata.getName();
        }
    }
}
