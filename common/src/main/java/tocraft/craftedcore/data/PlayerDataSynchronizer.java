package tocraft.craftedcore.data;

import dev.architectury.networking.NetworkManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.network.client.ClientNetworking;
import tocraft.craftedcore.registration.PlayerDataRegistry;

public class PlayerDataSynchronizer {
    private static final String PLAYER_DATA_SYNC = "player_data_sync";
    private static final ResourceLocation PLAYER_DATA_SYNC_ID = CraftedCore.id(PLAYER_DATA_SYNC);
    private static final CustomPacketPayload.Type<PacketPayload> PACKET_TYPE = new CustomPacketPayload.Type<>(PLAYER_DATA_SYNC_ID);
    private static final StreamCodec<RegistryFriendlyByteBuf, PacketPayload> PACKET_CODEC = CustomPacketPayload.codec(PacketPayload::write, PacketPayload::new);

    public static void registerPacketHandler() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_TYPE, PACKET_CODEC, (packet, context) -> {
            CompoundTag tag = packet.nbt();
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
        NetworkManager.sendToPlayer(player, new PacketPayload(tag));
    }

    private record PacketPayload(CompoundTag nbt) implements CustomPacketPayload {
        public PacketPayload(RegistryFriendlyByteBuf buf) {
            this(buf.readNbt());
        }

        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeNbt(nbt);
        }

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return PACKET_TYPE;
        }
    }
}
