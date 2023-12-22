package tocraft.craftedcore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import tocraft.craftedcore.client.CraftedCoreClient;
import tocraft.craftedcore.config.ConfigLoader;
import tocraft.craftedcore.events.common.PlayerEvents;
import tocraft.craftedcore.platform.Platform;
import tocraft.craftedcore.platform.VersionChecker;

public class CraftedCore {

	public static final Logger LOGGER = LoggerFactory.getLogger(CraftedCore.class);
	public static final String MODID = "craftedcore";
	private static String versionURL = "https://raw.githubusercontent.com/ToCraft/craftedcore/1.20.2/gradle.properties";
	
	public void initialize() {
		// ensure the client will receive and handle the configuration package 
		if (Platform.getDist().isClient()) {
			new CraftedCoreClient().initialize();
		}
		
		VersionChecker.registerChecker(MODID, versionURL, Component.literal("CraftedCore"));
		
		PlayerEvents.PLAYER_JOIN.register(player -> {			
			// send configurations to client
			ConfigLoader.sendConfigSyncPackages(player);
		});
	}
	
	public static ResourceLocation id(String name) {
		return new ResourceLocation(MODID, name);
	}
}
