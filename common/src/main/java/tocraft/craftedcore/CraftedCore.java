package tocraft.craftedcore;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.api.EnvType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tocraft.craftedcore.config.ConfigLoader;
import tocraft.craftedcore.data.PlayerDataSynchronizer;
import tocraft.craftedcore.event.common.PlayerEvents;
import tocraft.craftedcore.network.ModernNetworking;
import tocraft.craftedcore.platform.PlatformData;
import tocraft.craftedcore.platform.PlayerProfile;
import tocraft.craftedcore.platform.VersionChecker;
import tocraft.craftedcore.registration.SynchronizedReloadListenerRegistry;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class CraftedCore {
    @ApiStatus.Internal
    public static final Logger LOGGER = LoggerFactory.getLogger(CraftedCore.class);
    @ApiStatus.Internal
    public final static Path CACHE_DIR = PlatformData.getConfigPath().resolve(CraftedCore.MODID + File.separatorChar + "cache");
    @ApiStatus.Internal
    public static final ResourceLocation CLEAR_CACHE_PACKET = id("clear_cache");
    public static final String MODID = "craftedcore";

    public void initialize() {
        // initialize MixinExtras
        MixinExtrasBootstrap.init();

        // register Network Types
        ModernNetworking.registerType(ConfigLoader.CONFIG_SYNC);
        ModernNetworking.registerType(PlayerDataSynchronizer.PLAYER_DATA_SYNC_ID);

        // cache patreons in an extra thread to prevent longer loading times while connecting
        CompletableFuture.runAsync(VIPs::cachePatreons);

        // send configurations to client
        PlayerEvents.PLAYER_JOIN.register(ConfigLoader::sendConfigSyncPackages);

        PlayerEvents.PLAYER_JOIN.register(player -> {
            //noinspection resource
            for (ServerPlayer serverPlayer : player.serverLevel().players()) {
                PlayerDataSynchronizer.sync(serverPlayer);
            }
        });

        // sync data pack packets on data pack sync
        SynchronizedReloadListenerRegistry.initialize();

        // check for new version
        VersionChecker.registerModrinthChecker(MODID, "crafted-core", Component.literal("CraftedCore"));

        ModernNetworking.registerType(CLEAR_CACHE_PACKET);
        PlayerProfile.initialize();
        CraftedCoreCommand.initialize();

        // register config screen
        if (PlatformData.getEnv() == EnvType.CLIENT) {
            PlatformData.registerConfigScreen(CraftedCoreConfig.INSTANCE.getName());
        }
    }

    private static boolean hasReportedNoInternet = false;

    public static void reportMissingInternet(Throwable cause) {
        if (!hasReportedNoInternet) {
            CraftedCore.LOGGER.error("No internet connection!", cause);
            hasReportedNoInternet = true;
        }
    }

    @Contract("_ -> new")
    @ApiStatus.Internal
    public static @NotNull ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }

    @ApiStatus.Internal
    public static void clearCache() {
        PlayerProfile.clearCache();
        VIPs.clearCache();
        forceDeleteFile(CACHE_DIR.toFile());

        CompletableFuture.runAsync(VIPs::cachePatreons);
    }

    @ApiStatus.Internal
    public static void forceDeleteFile(@NotNull File element) {
        if (element.exists()) {
            if (element.isDirectory()) {
                File[] subFiles = element.listFiles();
                if (subFiles != null) {
                    for (File sub : subFiles) {
                        forceDeleteFile(sub);
                    }
                }
            }
            //noinspection ResultOfMethodCallIgnored
            element.delete();
        }
    }

}
