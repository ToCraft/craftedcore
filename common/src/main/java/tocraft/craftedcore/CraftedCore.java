package tocraft.craftedcore;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.resources.ResourceLocation;

public class CraftedCore {

	public static final Logger LOGGER = LoggerFactory.getLogger(CraftedCore.class);
	public static final String MODID = "craftedcore";
	private static String VERSION = "";
	public static List<String> devs = new ArrayList<>();

	static {
		devs.add("1f63e38e-4059-4a4f-b7c4-0fac4a48e744");
	}

	public void initialize() {
	}


	public static ResourceLocation id(String name) {
		return new ResourceLocation(MODID, name);
	}

	public static void setVersion(String version) {
		VERSION = version;
	}

	public static String getVersion() {
		return VERSION;
	}
}
