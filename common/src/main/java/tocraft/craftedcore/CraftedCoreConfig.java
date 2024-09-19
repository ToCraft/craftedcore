package tocraft.craftedcore;

import tocraft.craftedcore.config.Config;
import tocraft.craftedcore.config.ConfigLoader;
import tocraft.craftedcore.config.annotions.Comment;

@SuppressWarnings("CanBeFinal")
public final class CraftedCoreConfig implements Config {
    public static final CraftedCoreConfig INSTANCE = ConfigLoader.register(CraftedCore.MODID);
    @Comment("Toggle the version checking for every mod that uses CraftedCore")
    public boolean enableVersionChecking = true;
    @Comment("Update cached Player Profiles in the background")
    public boolean autoUpdateCache = true;

    @Override
    public String getName() {
        return CraftedCore.MODID;
    }
}
