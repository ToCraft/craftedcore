package tocraft.craftedcore.data;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.network.NetworkManager;
import tocraft.craftedcore.network.client.ClientNetworking;
import tocraft.craftedcore.registration.PlayerDataRegistry;

public class PlayerDataSynchronizer {
	private static String PLAYER_DATA_SYNC = "player_data_sync";
	
	public static void registerPacketHandler() {
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, CraftedCore.id(PLAYER_DATA_SYNC), (packet, context) -> {
			CompoundTag tag = packet.readNbt();
			ListTag list = (ListTag) tag.get(PLAYER_DATA_SYNC);
			list.forEach(entry -> {
				((CompoundTag) entry).getAllKeys().forEach(key -> {
					ClientNetworking.runOrQueue(context, player -> {
						((PlayerDataProvider) player).writePlayerData(key, ((CompoundTag) entry).get(key));
					});
				});
			});
		});
	}
	
	/**
	 * Synchronize data from the server to the client
	 */
	public static void sync(ServerPlayer player) {
        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        ((PlayerDataProvider) player).foreachKeyAndValue((key, value) -> {
        	// ignore key if it shouldn't be synchronized to the client
        	if (!PlayerDataRegistry.shouldKeySync(key))
        		return;
        	
        	CompoundTag entry = new CompoundTag();
        	entry.put(key, value);
        	list.add(entry);
        });
        tag.put(PLAYER_DATA_SYNC, list);
        packet.writeNbt(tag);
        NetworkManager.sendToPlayer(player, CraftedCore.id(PLAYER_DATA_SYNC), packet);
    }
}
