package tocraft.craftedcore.platform.neoforge;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforgespi.language.IModInfo;
import tocraft.craftedcore.platform.Dist;
import tocraft.craftedcore.platform.Mod;

public class PlatformImpl {
	
	public static Path getConfigPath() {
		return FMLPaths.CONFIGDIR.get();
	}
	
    public static boolean isModLoaded(String id) {
        return ModList.get().isLoaded(id);
    }
    
    public static Mod getMod(String id) {
        return new ModImpl(id);
        
    }
    
    public static Collection<Mod> getMods() {
    	Collection<Mod> mods = new HashSet<Mod>();
    	ModList.get().getMods().forEach(mod -> {
    		mods.add(getMod(mod.getModId()));
    	});
    	
    	return mods;
    }
    
    public static Dist getDist() {
    	return FMLEnvironment.dist.isClient() ? Dist.CLIENT : Dist.DEDICATED_SERVER;
    }
    
    private static class ModImpl implements Mod {
        private final ModContainer container;
        private final IModInfo metadata;
        
		public ModImpl(String id) {
            this.container = ModList.get().getModContainerById(id).orElseThrow();
            this.metadata = this.container.getModInfo();
        }
        
        @Override
        public String getModId() {
            return container.getModId();
        }
        
        @Override
        public String getVersion() {
            return metadata.getVersion().toString();
        }
        
        @Override
        public String getName() {
            return metadata.getDisplayName();
        }
    }
}
