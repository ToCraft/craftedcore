package tocraft.craftedcore.forge;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.events.common.forge.ForgeEventHandler;
import tocraft.craftedcore.network.NetworkManager;
import tocraft.craftedcore.network.forge.NetworkManagerImpl;
import tocraft.craftedcore.platform.Platform;

@Mod(CraftedCore.MODID)
public class CraftedCoreForge {

	public CraftedCoreForge() {
		MinecraftForge.EVENT_BUS.register(ForgeEventHandler.class);
		MinecraftForge.EVENT_BUS.register(NetworkManagerImpl.class);
		
		if (Platform.getDist().isClient())
			new CraftedCoreForgeClient();
		
		new CraftedCore().initialize();
	}
	
	public static void sendSwapRequest() {
		NetworkManager.sendToServer( new ResourceLocation(CraftedCore.MODID, "test"), new FriendlyByteBuf(Unpooled.buffer()));
	}
}
