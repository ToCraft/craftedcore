package tocraft.craftedcore.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.network.ModernNetworking;
import tocraft.craftedcore.network.client.ClientNetworking;
import tocraft.craftedcore.registration.PlayerDataRegistry;

public class PlayerDataSynchronizer {
    private static final String PLAYER_DATA_SYNC = "player_data_sync";
    private static final ResourceLocation PLAYER_DATA_SYNC_ID = CraftedCore.id(PLAYER_DATA_SYNC);

    public static void registerPacketHandler() {
        ModernNetworking.registerReceiver(ModernNetworking.Side.S2C, PLAYER_DATA_SYNC_ID, (context, tag) -> {
            if (tag != null) {
                ListTag list = (ListTag) tag.get(PLAYER_DATA_SYNC);
                if (list != null) {
                    for (Tag entry : list) {
                        for (String key : ((CompoundTag) entry).getAllKeys()) {
                            ClientNetworking.runOrQueue(context, player -> ((PlayerDataProvider) player).craftedcore$writeTag(key, ((CompoundTag) entry).get(key)));
                        }
                    }
                }
            }
        });
    }

    /**
     * Synchronize data from the server to the client
     */
    public static void sync(ServerPlayer player) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();

        PlayerDataProvider playerData = ((PlayerDataProvider) player);

        for (String key : ((PlayerDataProvider) player).craftedcore$keySet()) {
            // ignore key if it shouldn't be synchronized to the client
            if (!PlayerDataRegistry.shouldSyncKey(key))
                return;

            CompoundTag entry = new CompoundTag();
            entry.put(key, playerData.craftedcore$readTag(key));
            list.add(entry);
        }
        tag.put(PLAYER_DATA_SYNC, list);
        ModernNetworking.sendToPlayer(player, PLAYER_DATA_SYNC_ID, tag);
    }
}
