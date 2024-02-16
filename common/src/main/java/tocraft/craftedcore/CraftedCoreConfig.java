package tocraft.craftedcore;

import tocraft.craftedcore.config.Config;

public class CraftedCoreConfig implements Config {
    public boolean enableVersionChecking = true;

    @Override
    public String getName() {
        return CraftedCore.MODID;
    }
}
