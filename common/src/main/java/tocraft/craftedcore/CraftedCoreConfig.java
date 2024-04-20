package tocraft.craftedcore;

import tocraft.craftedcore.config.Config;
import tocraft.craftedcore.config.ConfigLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class CraftedCoreConfig implements Config {
    public static final CraftedCoreConfig INSTANCE = ConfigLoader.read(CraftedCore.MODID, CraftedCoreConfig.class);
    public boolean enableVersionChecking = true;
    public boolean savePatreonsLocal = false;

    public List<UUID> localPatreonsList = new ArrayList<>();

    @Override
    public String getName() {
        return CraftedCore.MODID;
    }
}
