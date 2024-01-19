package tocraft.craftedcore;

import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tocraft.craftedcore.config.ConfigLoader;
import tocraft.craftedcore.platform.VersionChecker;

public class CraftedCore {

    public static final Logger LOGGER = LoggerFactory.getLogger(CraftedCore.class);
    public static final String MODID = "craftedcore";
    private static final String versionURL = "https://raw.githubusercontent.com/ToCraft/craftedcore/1.20.2/gradle.properties";

    public void initialize() {
        VersionChecker.registerChecker(MODID, versionURL, Component.literal("CraftedCore"));

        // send configurations to client
        PlayerEvent.PLAYER_JOIN.register(ConfigLoader::sendConfigSyncPackages);
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MODID, name);
    }
}
