package tocraft.craftedcore.data;

import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.network.ModernNetworking;
import tocraft.craftedcore.network.client.ClientNetworking;
import tocraft.craftedcore.patched.CEntity;
import tocraft.craftedcore.registration.PlayerDataRegistry;

public class PlayerDataSynchronizer {
    private static final String PLAYER_DATA_SYNC = "player_data_sync";
    public static final ResourceLocation PLAYER_DATA_SYNC_ID = CraftedCore.id(PLAYER_DATA_SYNC);

    public static void registerPacketHandler() {
        ModernNetworking.registerReceiver(ModernNetworking.Side.S2C, PLAYER_DATA_SYNC_ID, (context, tag) -> {
            if (tag != null) {
                ListTag list = (ListTag) tag.get(PLAYER_DATA_SYNC);
                if (list != null) {
                    for (Tag entry : list) {
                        if (entry instanceof CompoundTag compoundTag) {
                            for (String key : compoundTag.getAllKeys()) {
                                ClientNetworking.runOrQueue(context, player -> {
                                    PlayerDataProvider playerDataProvider;
                                    if (tag.hasUUID("uuid")) {
                                        playerDataProvider = (PlayerDataProvider) player.getCommandSenderWorld().getPlayerByUUID(tag.getUUID("uuid"));
                                    } else {
                                        playerDataProvider = (PlayerDataProvider) player;
                                    }
                                    if (playerDataProvider != null) {
                                        playerDataProvider.craftedcore$writeTag(key, ((CompoundTag) entry).get(key));
                                    }
                                });
                            }
                        } else if (entry instanceof StringTag stringTag) {
                            ClientNetworking.runOrQueue(context, player -> {
                                PlayerDataProvider playerDataProvider;
                                if (tag.hasUUID("uuid")) {
                                    playerDataProvider = (PlayerDataProvider) player.getCommandSenderWorld().getPlayerByUUID(tag.getUUID("uuid"));
                                } else {
                                    playerDataProvider = (PlayerDataProvider) player;
                                }
                                if (playerDataProvider != null) {
                                    playerDataProvider.craftedcore$writeTag(stringTag.getAsString(), null);
                                }
                            });
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
            entry.put(key, value);
            list.add(entry);
        }
        tag.put(PLAYER_DATA_SYNC, list);

        ModernNetworking.sendToPlayer(player, PLAYER_DATA_SYNC_ID, tag);
    }

    private static void syncToAll(ServerPlayer player) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();

        tag.putUUID("uuid", player.getUUID());
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
                list.add(StringTag.valueOf(key));
            }
            list.add(entry);
        }
        tag.put(PLAYER_DATA_SYNC, list);
        //noinspection resource
        ModernNetworking.sendToPlayers(((ServerLevel) CEntity.level(player)).players(), PLAYER_DATA_SYNC_ID, tag);
    }
}
