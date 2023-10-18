package tocraft.craftedcore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.network.chat.Component;
import tocraft.craftedcore.events.common.PlayerEvents;
import tocraft.craftedcore.platform.Platform;
import tocraft.craftedcore.platform.VersionChecker;

public class CraftedCore {

	public static final Logger LOGGER = LoggerFactory.getLogger(CraftedCore.class);
	public static final String MODID = "craftedcore";
	private static String versionURL = "https://raw.githubusercontent.com/ToCraft/craftedcore/1.20.2/gradle.properties";

	public void initialize() {
		PlayerEvents.PLAYER_JOIN.register(player -> {
			// get newest version from url
			String newestVersion = VersionChecker.checkForNewVersion(versionURL);
			// Warns in the log, if checking failed
			if (newestVersion == null)
				CraftedCore.LOGGER.warn("Version check failed");
			// Notifies the joined player, that newer version is available
			else if (!newestVersion.equals(CraftedCore.getVersion())) {
				player.sendSystemMessage(Component.translatable("craftedcore.update", newestVersion));
			}
		});
	}

	public static String getVersion() {
		return Platform.getMod(MODID).getVersion();
	}
}
