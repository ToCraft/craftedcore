package tocraft.craftedcore;

import tocraft.craftedcore.config.Config;
import tocraft.craftedcore.config.ConfigLoader;

@SuppressWarnings("CanBeFinal")
public final class CraftedCoreConfig implements Config {
    public static final CraftedCoreConfig INSTANCE = ConfigLoader.read(CraftedCore.MODID, CraftedCoreConfig.class);
    public boolean enableVersionChecking = true;

    @Override
    public String getName() {
        return CraftedCore.MODID;
    }
}
