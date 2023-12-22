package tocraft.craftedcore.network.neoforge;

import java.util.Collections;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import tocraft.craftedcore.network.NetworkManager;

@OnlyIn(Dist.CLIENT)
public class ClientNetworkingManager {
    public static void initClient() {
        NetworkManagerImpl.CHANNEL.addListener(NetworkManagerImpl.createPacketHandler(PlayNetworkDirection.PLAY_TO_CLIENT, NetworkManagerImpl.S2C_TRANSFORMERS));
        NeoForge.EVENT_BUS.register(ClientNetworkingManager.class);
        
        NetworkManagerImpl.registerS2CReceiver(NetworkManagerImpl.SYNC_IDS, Collections.emptyList(), (buffer, context) -> {
            Set<ResourceLocation> receivables = NetworkManagerImpl.serverReceivables;
            int size = buffer.readInt();
            receivables.clear();
            for (int i = 0; i < size; i++) {
                receivables.add(buffer.readResourceLocation());
            }
            context.queue(() -> {
                NetworkManager.sendToServer(NetworkManagerImpl.SYNC_IDS, NetworkManagerImpl.sendSyncPacket(NetworkManagerImpl.C2S));
            });
        });
    }
    
    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }
    
    @SubscribeEvent
    public static void loggedOut(ClientPlayerNetworkEvent.LoggingOut event) {
        NetworkManagerImpl.serverReceivables.clear();
    }
}
