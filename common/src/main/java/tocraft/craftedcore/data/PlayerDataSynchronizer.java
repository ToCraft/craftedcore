package tocraft.craftedcore.data;

import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.network.ModernNetworking;
import tocraft.craftedcore.network.client.ClientNetworking;
import tocraft.craftedcore.registration.PlayerDataRegistry;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PlayerDataSynchronizer {
    private static final String PLAYER_DATA_SYNC = "player_data_sync";
    public static final ResourceLocation PLAYER_DATA_SYNC_ID = CraftedCore.id(PLAYER_DATA_SYNC);

    public static void registerPacketHandler() {
        ModernNetworking.registerReceiver(ModernNetworking.Side.S2C, PLAYER_DATA_SYNC_ID, (context, tag) -> {
            if (tag != null) {
                ListTag list = (ListTag) tag.get(PLAYER_DATA_SYNC);
                if (list != null) {
                    for (Tag entry : list) {
                        for (String key : ((CompoundTag) entry).keySet()) {
                            if (Objects.equals(key, "DELETED")) {
                                ClientNetworking.runOrQueue(context, player -> {
                                    PlayerDataProvider playerDataProvider;
                                    Optional<int[]> uuidia = tag.getIntArray("uuid");
                                    if (uuidia.isPresent()) {
                                        UUID uuid = UUIDUtil.uuidFromIntArray(uuidia.get());
                                        playerDataProvider = (PlayerDataProvider) player.getCommandSenderWorld().getPlayerByUUID(uuid);
                                    } else {
                                        playerDataProvider = (PlayerDataProvider) player;
                                    }
                                    if (playerDataProvider != null) {
                                        String tkey = Objects.requireNonNull(((CompoundTag) entry).get(key)).asString().orElseThrow();
                                        playerDataProvider.craftedcore$writeTag(tkey, null);
                                    }
                                });
                            } else {
                                ClientNetworking.runOrQueue(context, player -> {
                                    PlayerDataProvider playerDataProvider;
                                    Optional<int[]> uuidia = tag.getIntArray("uuid");
                                    if (uuidia.isPresent()) {
                                        UUID uuid = UUIDUtil.uuidFromIntArray(uuidia.get());
                                        playerDataProvider = (PlayerDataProvider) player.getCommandSenderWorld().getPlayerByUUID(uuid);
                                    } else {
                                        playerDataProvider = (PlayerDataProvider) player;
                                    }
                                    if (playerDataProvider != null) {
                                        playerDataProvider.craftedcore$writeTag(key, ((CompoundTag) entry).get(key));
                                    }
                                });
                            }
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
        syncToSelf(player);
        syncToAll(player);
    }

    private static void syncToSelf(ServerPlayer player) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();

        PlayerDataProvider playerData = ((PlayerDataProvider) player);

        for (String key : ((PlayerDataProvider) player).craftedcore$keySet()) {
            // ignore key if it shouldn't be synchronized to the client
            if (!PlayerDataRegistry.shouldSyncTagToSelf(key)) {
                return;
            }

            CompoundTag entry = new CompoundTag();
            Tag value = playerData.craftedcore$readTag(key);
            if (value != null) {
                entry.put(key, value);
            } else {
                entry.put("DELETED", StringTag.valueOf(key));
            }
            list.add(entry);
        }
        tag.put(PLAYER_DATA_SYNC, list);

        ModernNetworking.sendToPlayer(player, PLAYER_DATA_SYNC_ID, tag);
    }

    private static void syncToAll(@NotNull ServerPlayer player) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();

        UUID uuid = player.getUUID();
        int[] uuidia = UUIDUtil.uuidToIntArray(uuid);
        tag.putIntArray("uuid", uuidia);
        PlayerDataProvider playerData = ((PlayerDataProvider) player);

        for (String key : ((PlayerDataProvider) player).craftedcore$keySet()) {
            // ignore key if it shouldn't be synchronized to the client
            if (!PlayerDataRegistry.shouldSyncTagToAll(key)) {
                return;
            }

            CompoundTag entry = new CompoundTag();
            Tag value = playerData.craftedcore$readTag(key);
            if (value != null) {
                entry.put(key, value);
            } else {
                entry.put("DELETED", StringTag.valueOf(key));
            }
            list.add(entry);
        }
        tag.put(PLAYER_DATA_SYNC, list);
        //noinspection resource
        ModernNetworking.sendToPlayers(player.serverLevel().players(), PLAYER_DATA_SYNC_ID, tag);
    }
}
