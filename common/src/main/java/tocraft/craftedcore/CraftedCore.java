package tocraft.craftedcore;

import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tocraft.craftedcore.config.ConfigLoader;
import tocraft.craftedcore.platform.VersionChecker;

import java.net.MalformedURLException;
import java.net.URL;

public class CraftedCore {
    public static final Logger LOGGER = LoggerFactory.getLogger(CraftedCore.class);
    public static final String MODID = "craftedcore";
    private static final String MAVEN_URL = "https://maven.tocraft.dev/public/dev/tocraft/craftedcore/maven-metadata.xml";

    public void initialize() {
        // cache patreons in an extra thread to prevent longer loading times while connecting
        new Thread(VIPs::getCachedPatreons).start();

        // send configurations to client
        PlayerEvent.PLAYER_JOIN.register(ConfigLoader::sendConfigSyncPackages);

        // check for new version
        try {
            VersionChecker.registerMavenChecker(MODID, new URL(MAVEN_URL), Component.literal("CraftedCore"));
        } catch (MalformedURLException e) {
            CraftedCore.LOGGER.error("Failed to register the version checker", e);
        }
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MODID, name);
    }
}
