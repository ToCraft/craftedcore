package tocraft.craftedcore.forge;

import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import tocraft.craftedcore.CraftedCore;

@Mod(CraftedCore.MODID)
public class CraftedCoreForge {

	public CraftedCoreForge() {
		getModVersion();
		new CraftedCore().initialize();
	}

	public void getModVersion() {
		ModContainer modContainer = ModList.get().getModContainerById(CraftedCore.MODID).get();
		CraftedCore.setVersion(modContainer.getModInfo().getVersion().toString());
	}
	
	public class MyStaticForgeEventHandler {
	    @SubscribeEvent(priority = EventPriority.HIGH)
	    public static void arrowNocked(PlayerLoggedInEvent event) {
	        System.out.println("Arrow nocked!");
	        event.setResult(Result.ALLOW);
	    }
	}
}
