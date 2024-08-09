package tocraft.craftedcore;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tocraft.craftedcore.config.ConfigLoader;
import tocraft.craftedcore.data.PlayerDataSynchronizer;
import tocraft.craftedcore.event.common.PlayerEvents;
import tocraft.craftedcore.network.ModernNetworking;
import tocraft.craftedcore.patched.CEntity;
import tocraft.craftedcore.patched.Identifier;
import tocraft.craftedcore.patched.TComponent;
import tocraft.craftedcore.platform.VersionChecker;
import tocraft.craftedcore.registration.SynchronizedReloadListenerRegistry;

import java.util.concurrent.CompletableFuture;

public class CraftedCore {
    public static final Logger LOGGER = LoggerFactory.getLogger(CraftedCore.class);
    public static final String MODID = "craftedcore";

    public void initialize() {
        // initialize MixinExtras
        MixinExtrasBootstrap.init();

        //#if MC>=1205
        // register Network Types
        ModernNetworking.registerType(ConfigLoader.CONFIG_SYNC);
        ModernNetworking.registerType(PlayerDataSynchronizer.PLAYER_DATA_SYNC_ID);
        //#endif

        // cache patreons in an extra thread to prevent longer loading times while connecting
        CompletableFuture.runAsync(VIPs::cachePatreons);

        // send configurations to client
        PlayerEvents.PLAYER_JOIN.register(ConfigLoader::sendConfigSyncPackages);

        PlayerEvents.PLAYER_JOIN.register(player -> {
            for (ServerPlayer serverPlayer : ((ServerLevel) CEntity.level(player)).players()) {
                PlayerDataSynchronizer.sync(serverPlayer);
            }
        });

        // sync data pack packets on data pack sync
        SynchronizedReloadListenerRegistry.initialize();

        // check for new version
        VersionChecker.registerModrinthChecker(MODID, "crafted-core", TComponent.literal("CraftedCore"));
    }

    public static ResourceLocation id(String name) {
        return Identifier.parse(MODID, name);
    }
}
