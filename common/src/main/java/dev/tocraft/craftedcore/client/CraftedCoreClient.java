package dev.tocraft.craftedcore.client;

import dev.tocraft.craftedcore.CraftedCore;
import dev.tocraft.craftedcore.config.ConfigLoader;
import dev.tocraft.craftedcore.data.PlayerDataSynchronizer;
import dev.tocraft.craftedcore.event.client.ClientPlayerEvents;
import dev.tocraft.craftedcore.event.client.RenderEvents;
import dev.tocraft.craftedcore.network.ModernNetworking;
import dev.tocraft.craftedcore.network.client.ClientNetworking.ApplicablePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;

import java.util.HashSet;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class CraftedCoreClient {
    private static final Set<ApplicablePacket> SYNC_PACKET_QUEUE = new HashSet<>();

    public void initialize() {
        ConfigLoader.registerConfigSyncHandler();
        PlayerDataSynchronizer.registerPacketHandler();

        ClientPlayerEvents.CLIENT_PLAYER_JOIN.register(player -> {
            for (ApplicablePacket packet : getSyncPacketQueue()) {
                packet.apply(player);
            }

            getSyncPacketQueue().clear();
        });

        // prevent full air bar when swimming
        RenderEvents.RENDER_BREATH.register((graphics, player) -> {
            if (player != null && player.getAirSupply() == player.getMaxAirSupply() && player.isEyeInFluid(FluidTags.WATER)) {
                return InteractionResult.FAIL;
            } else {
                return InteractionResult.PASS;
            }
        });


        ModernNetworking.registerReceiver(ModernNetworking.Side.S2C, CraftedCore.CLEAR_CACHE_PACKET, (context, data) -> CraftedCore.clearCache());
    }

    public static Set<ApplicablePacket> getSyncPacketQueue() {
        return SYNC_PACKET_QUEUE;
    }
}
