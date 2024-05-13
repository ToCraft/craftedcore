package tocraft.craftedcore;

import net.fabricmc.api.EnvType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tocraft.craftedcore.config.ConfigLoader;
import tocraft.craftedcore.data.PlayerDataSynchronizer;
import tocraft.craftedcore.event.common.PlayerEvents;
import tocraft.craftedcore.network.ModernNetworking;
import tocraft.craftedcore.platform.PlatformData;
import tocraft.craftedcore.platform.VersionChecker;
import tocraft.craftedcore.registration.SynchronizedReloadListenerRegistry;

public class CraftedCore {
    public static final Logger LOGGER = LoggerFactory.getLogger(CraftedCore.class);
    public static final String MODID = "craftedcore";

    public void initialize() {
        // register Network Types
        //if (PlatformData.getEnv() == EnvType.SERVER) {
            ModernNetworking.registerType(ConfigLoader.CONFIG_SYNC);
            ModernNetworking.registerType(PlayerDataSynchronizer.PLAYER_DATA_SYNC_ID);
        //}

        // cache patreons in an extra thread to prevent longer loading times while connecting
        new Thread(VIPs::cachePatreons).start();

        // send configurations to client
        PlayerEvents.PLAYER_JOIN.register(ConfigLoader::sendConfigSyncPackages);

        // sync data pack packets on player join
        PlayerEvents.PLAYER_JOIN.register(SynchronizedReloadListenerRegistry::sendAllToPlayer);

        // check for new version
        VersionChecker.registerModrinthChecker(MODID, "crafted-core", Component.literal("CraftedCore"));
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MODID, name);
    }
}
