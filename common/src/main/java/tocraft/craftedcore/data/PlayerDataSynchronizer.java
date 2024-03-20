package tocraft.craftedcore.data;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.network.client.ClientNetworking;
import tocraft.craftedcore.registration.PlayerDataRegistry;

public class PlayerDataSynchronizer {
    private static final String PLAYER_DATA_SYNC = "player_data_sync";

    public static void registerPacketHandler() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CraftedCore.id(PLAYER_DATA_SYNC), (packet, context) -> {
            CompoundTag tag = packet.readNbt();
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
        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
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
        packet.writeNbt(tag);
        NetworkManager.sendToPlayer(player, CraftedCore.id(PLAYER_DATA_SYNC), packet);
    }
}
